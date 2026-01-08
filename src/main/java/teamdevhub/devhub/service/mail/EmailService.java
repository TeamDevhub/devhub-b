package teamdevhub.devhub.service.mail;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailVerificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.common.enums.EmailTemplateType;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.mail.EmailVerification;
import teamdevhub.devhub.port.in.mail.EmailVerificationUseCase;
import teamdevhub.devhub.port.out.mail.EmailVerificationRepository;
import teamdevhub.devhub.port.out.mail.EmailNotificationSender;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.common.provider.verification.EmailVerificationCodeProvider;
import teamdevhub.devhub.service.common.exception.BusinessRuleException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService implements EmailVerificationUseCase {

    private final EmailNotificationSender emailNotificationSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationCodeProvider emailVerificationCodeProvider;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public void sendEmailCertificationCode(EmailVerificationRequestDto emailVerificationRequestDto) {
        String email = emailVerificationRequestDto.getEmail();

        if (emailVerificationRepository.existUnexpiredCode(email)) {
            throw AuthRuleException.of(ErrorCode.EMAIL_VERIFICATION_ALREADY_SENT);
        }

        String emailCertificationCode = emailVerificationCodeProvider.generateEmailCertificationCode();
        LocalDateTime expiredAt = dateTimeProvider.now().plus(Duration.ofMinutes(5));
        EmailVerification emailVerification = EmailVerification.issue(email, emailCertificationCode, expiredAt);
        emailVerificationRepository.save(emailVerification);

        Map<String, Object> emailCertificationVariables = Map.of(EmailTemplateVariables.CODE, emailCertificationCode, EmailTemplateVariables.EXPIRE_TIME, EmailTemplateType.EMAIL_CERTIFICATION.getExpireTime());
        emailNotificationSender.sendEmail(email, EmailTemplateType.EMAIL_CERTIFICATION, emailCertificationVariables);
    }

    @Override
    public void confirmEmailCertificationCode(ConfirmEmailVerificationCommand confirmEmailVerificationCommand) {
        EmailVerification emailVerification = emailVerificationRepository.getByEmail(confirmEmailVerificationCommand.getEmail());

        boolean verified = emailVerification.verify(
                confirmEmailVerificationCommand.getCode(),
                dateTimeProvider.now()
        );

        if (!verified) {
            throw BusinessRuleException.of(ErrorCode.EMAIL_NOT_CONFIRMED);
        }

        emailVerificationRepository.save(emailVerification);
    }

    @Override
    public boolean isVerified(String email) {
        EmailVerification emailVerification = emailVerificationRepository.getByEmail(email);
        return emailVerification.isVerified() && emailVerification.getExpiredAt().isAfter(dateTimeProvider.now());
    }
}