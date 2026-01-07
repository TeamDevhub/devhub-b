package teamdevhub.devhub.service.mail;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailCertificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailCertificationRequestDto;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.common.enums.EmailTemplateType;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.common.record.mail.EmailCertification;
import teamdevhub.devhub.port.in.mail.EmailCertificationUseCase;
import teamdevhub.devhub.port.out.mail.EmailCertificationRepository;
import teamdevhub.devhub.port.out.mail.EmailNotificationSender;
import teamdevhub.devhub.port.out.provider.DateTimeProvider;
import teamdevhub.devhub.port.out.provider.EmailCertificationCodeProvider;
import teamdevhub.devhub.service.common.exception.BusinessRuleException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService implements EmailCertificationUseCase {

    private final EmailNotificationSender emailNotificationSender;
    private final EmailCertificationRepository emailCertificationRepository;
    private final EmailCertificationCodeProvider emailCertificationCodeProvider;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public void sendEmailCertificationCode(EmailCertificationRequestDto emailCertificationRequestDto) {
        String email = emailCertificationRequestDto.getEmail();

        if (emailCertificationRepository.existUnexpiredCode(email)) {
            throw AuthRuleException.of(ErrorCode.EMAIL_CERTIFICATION_CODE_ALREADY_SENT);
        }

        String emailCertificationCode = emailCertificationCodeProvider.generateEmailCertificationCode();
        LocalDateTime expiredAt = dateTimeProvider.now().plus(Duration.ofMinutes(5));
        EmailCertification emailCertification = EmailCertification.of(email, emailCertificationCode, expiredAt, null);
        emailCertificationRepository.save(emailCertification);

        Map<String, Object> emailCertificationVariables = Map.of(EmailTemplateVariables.CODE, emailCertificationCode, EmailTemplateVariables.EXPIRE_TIME, EmailTemplateType.EMAIL_CERTIFICATION.getExpireTime());
        emailNotificationSender.sendEmail(email, EmailTemplateType.EMAIL_CERTIFICATION, emailCertificationVariables);
    }

    @Override
    public void confirmEmailCertificationCode(ConfirmEmailCertificationCommand confirmEmailCertificationCommand) {
        boolean isVerified = verify(confirmEmailCertificationCommand.getEmail(), confirmEmailCertificationCommand.getCode());
        if (!isVerified) {
            throw BusinessRuleException.of(ErrorCode.EMAIL_NOT_CONFIRMED);
        }
    }

    @Override
    public boolean isVerified(String email) {
        EmailCertification emailCertification = emailCertificationRepository.findByEmail(email);
        return emailCertification.verifiedAt() != null && emailCertification.expiredAt().isAfter(dateTimeProvider.now());
    }

    @Override
    public void delete(String email) {
        emailCertificationRepository.delete(email);
    }

    private boolean verify(String email, String code) {
        EmailCertification emailCertification = emailCertificationRepository.findByEmail(email);

        if (emailCertification == null) {
            return false;
        }

        if (emailCertification.isExpired(dateTimeProvider.now())) {
            return false;
        }

        return emailCertification.code().equals(code);
    }

}