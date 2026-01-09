package teamdevhub.devhub.small.mock.usecase;

import teamdevhub.devhub.port.in.mail.command.ConfirmEmailVerificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.domain.mail.EmailVerification;
import teamdevhub.devhub.port.in.mail.EmailVerificationUseCase;
import teamdevhub.devhub.port.out.mail.EmailVerificationRepository;
import teamdevhub.devhub.service.exception.BusinessRuleException;

public class FakeEmailVerificationUseCase implements EmailVerificationUseCase {

    private final EmailVerificationRepository emailVerificationRepository;
    private final DateTimeProvider dateTimeProvider;

    public FakeEmailVerificationUseCase(EmailVerificationRepository emailVerificationRepository, DateTimeProvider dateTimeProvider) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public void sendEmailVerification(EmailVerificationRequestDto requestDto) {
        String email = requestDto.getEmail();

        if (emailVerificationRepository.existUnexpiredCode(email)) {
            throw AuthRuleException.of(ErrorCode.EMAIL_VERIFICATION_ALREADY_SENT);
        }

        EmailVerification emailVerification = EmailVerification.issue(
                email,
                "123456",
                dateTimeProvider.now().plusMinutes(5)
        );

        emailVerificationRepository.save(emailVerification);
    }

    @Override
    public void confirmEmailVerification(ConfirmEmailVerificationCommand confirmEmailVerificationCommand) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(confirmEmailVerificationCommand.getEmail());

        if(!emailVerification.verify(confirmEmailVerificationCommand.getCode(), dateTimeProvider.now())) {
            throw BusinessRuleException.of(ErrorCode.EMAIL_NOT_CONFIRMED);
        }

        emailVerificationRepository.save(emailVerification);
    }

    @Override
    public boolean isVerified(String email) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email);
        return emailVerification != null
                && emailVerification.isVerified()
                && emailVerification.getExpiredAt().isAfter(dateTimeProvider.now());
    }
}
