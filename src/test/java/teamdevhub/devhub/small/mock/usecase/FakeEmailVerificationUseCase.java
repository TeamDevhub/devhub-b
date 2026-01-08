package teamdevhub.devhub.small.mock.usecase;

import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailVerificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.port.in.mail.EmailVerificationUseCase;
import teamdevhub.devhub.port.out.mail.EmailVerificationRepository;
import teamdevhub.devhub.service.common.exception.BusinessRuleException;

public class FakeEmailVerificationUseCase implements EmailVerificationUseCase {

    private final EmailVerificationRepository emailVerificationRepository;
    private final DateTimeProvider dateTimeProvider;

    public FakeEmailVerificationUseCase(EmailVerificationRepository emailVerificationRepository, DateTimeProvider dateTimeProvider) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public void sendEmailCertificationCode(EmailVerificationRequestDto requestDto) {
        String email = requestDto.getEmail();

        if (emailVerificationRepository.existUnexpiredCode(email)) {
            throw AuthRuleException.of(ErrorCode.EMAIL_VERIFICATION_ALREADY_SENT);
        }

        EmailCertification emailCertification = EmailCertification.of(
                email,
                "123456",
                dateTimeProvider.now().plusMinutes(5),
                null
        );

        emailVerificationRepository.save(emailCertification);
    }

    @Override
    public void confirmEmailCertificationCode(ConfirmEmailVerificationCommand command) {
        EmailCertification emailCertification =
                repository.findByEmail(command.getEmail());

        if (emailCertification == null) {
            throw BusinessRuleException.of(ErrorCode.EMAIL_NOT_CONFIRMED);
        }

        if (emailCertification.isExpired(dateTimeProvider.now())) {
            throw BusinessRuleException.of(ErrorCode.EMAIL_NOT_CONFIRMED);
        }

        if (!emailCertification.code().equals(command.getCode())) {
            throw BusinessRuleException.of(ErrorCode.EMAIL_NOT_CONFIRMED);
        }

        EmailCertification verified =
                emailCertification.withVerifiedAt(dateTimeProvider.now());

        emailVerificationRepository.save(verified);
    }

    @Override
    public boolean isVerified(String email) {
        EmailCertification emailCertification = emailVerificationRepository.findByEmail(email);
        return emailCertification != null
                && emailCertification.verifiedAt() != null
                && emailCertification.expiredAt().isAfter(dateTimeProvider.now());
    }
}
