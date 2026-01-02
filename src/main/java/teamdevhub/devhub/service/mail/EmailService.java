package teamdevhub.devhub.service.mail;

import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailCertificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailCertificationRequestDto;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.record.mail.EmailCertification;
import teamdevhub.devhub.port.in.mail.EmailCertificationUseCase;
import teamdevhub.devhub.port.out.common.DateTimeProvider;
import teamdevhub.devhub.port.out.common.EmailCertificationCodeProvider;
import teamdevhub.devhub.port.out.mail.EmailCertificationRepository;
import teamdevhub.devhub.port.out.mail.EmailNotificationSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

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

        if (emailCertificationRepository.existsValidCode(email)) {
            throw AuthRuleException.of(ErrorCodeEnum.EMAIL_CERTIFICATION_CODE_ALREADY_SENT);
        }

        String code = emailCertificationCodeProvider.generateEmailCertificationCode();
        LocalDateTime expiredAt = dateTimeProvider.now().plus(Duration.ofMinutes(5));
        EmailCertification emailCertification = new EmailCertification(email, code, expiredAt, null);
        emailCertificationRepository.save(emailCertification);
        emailNotificationSender.sendEmail(email, "[회원가입] 이메일 인증 코드", code);
    }

    @Override
    public void confirmEmailCertificationCode(ConfirmEmailCertificationCommand confirmEmailCertificationCommand) {
        boolean verified = emailCertificationRepository.verify(confirmEmailCertificationCommand.getEmail(), confirmEmailCertificationCommand.getCode());
        if (!verified) {
            throw AuthRuleException.of(ErrorCodeEnum.EMAIL_NOT_CONFIRMED);
        }
    }

    @Override
    public boolean isVerified(String email) {
        return emailCertificationRepository.isVerified(email);
    }

    @Override
    public void delete(String email) {
        emailCertificationRepository.delete(email);
    }
}