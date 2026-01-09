package teamdevhub.devhub.service.mail;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.port.in.mail.command.ConfirmEmailVerificationCommand;
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
import teamdevhub.devhub.service.exception.BusinessRuleException;

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
    public void sendEmailVerification(EmailVerificationRequestDto emailVerificationRequestDto) {
        String email = emailVerificationRequestDto.getEmail();

        if (emailVerificationRepository.existUnexpiredCode(email)) {
            throw AuthRuleException.of(ErrorCode.EMAIL_VERIFICATION_ALREADY_SENT);
        }

        String emailVerificationCode = emailVerificationCodeProvider.generateEmailVerificationCode();
        LocalDateTime expiredAt = dateTimeProvider.now().plus(Duration.ofMinutes(5));
        EmailVerification emailVerification = EmailVerification.issue(email, emailVerificationCode, expiredAt);
        emailVerificationRepository.save(emailVerification);

        Map<String, Object> emailVerificationVariables = Map.of(EmailTemplateVariables.CODE, emailVerificationCode, EmailTemplateVariables.EXPIRE_TIME, EmailTemplateType.EMAIL_VERIFICATION.getExpireTime());
        emailNotificationSender.send(email, EmailTemplateType.EMAIL_VERIFICATION, emailVerificationVariables);
    }

    @Override
    public void confirmEmailVerification(ConfirmEmailVerificationCommand confirmEmailVerificationCommand) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(confirmEmailVerificationCommand.getEmail());

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
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email);
        return emailVerification.isVerified() && emailVerification.getExpiredAt().isAfter(dateTimeProvider.now());
    }
}