package teamdevhub.devhub.service.mail;

import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailCertificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailCertificationRequestDto;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.port.in.mail.EmailCertificationUseCase;
import teamdevhub.devhub.port.out.common.EmailCertificationCodeProvider;
import teamdevhub.devhub.port.out.mail.EmailCertificationPort;
import teamdevhub.devhub.port.out.mail.EmailSendPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService implements EmailCertificationUseCase {

    private final EmailSendPort emailSendPort;
    private final EmailCertificationPort emailCertificationPort;
    private final EmailCertificationCodeProvider emailCertificationCodeProvider;

    @Override
    public void sendEmailCertificationCode(EmailCertificationRequestDto emailCertificationRequestDto) {
        String email = emailCertificationRequestDto.getEmail();
        if (emailCertificationPort.existsValidCode(email)) {
            throw AuthRuleException.of(ErrorCodeEnum.EMAIL_CERTIFICATION_CODE_ALREADY_SENT);
        }

        String code = emailCertificationCodeProvider.generateEmailCertificationCode();
        emailCertificationPort.save(email, code, Duration.ofMinutes(5));
        emailSendPort.sendEmail(email, "[회원가입] 이메일 인증 코드", code);
    }

    @Override
    public void confirmEmailCertificationCode(ConfirmEmailCertificationCommand confirmEmailCertificationCommand) {
        boolean verified = emailCertificationPort.verify(confirmEmailCertificationCommand.getEmail(), confirmEmailCertificationCommand.getCode());
        if (!verified) {
            throw AuthRuleException.of(ErrorCodeEnum.EMAIL_NOT_CONFIRMED);
        }
    }

    @Override
    public boolean isVerified(String email) {
        return emailCertificationPort.isVerified(email);
    }

    @Override
    public void delete(String email) {
        emailCertificationPort.delete(email);
    }
}