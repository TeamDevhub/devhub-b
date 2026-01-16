package teamdevhub.devhub.adapter.out.common.sender;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import teamdevhub.devhub.adapter.out.exception.ExternalServiceException;
import teamdevhub.devhub.common.enums.EmailTemplateType;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.verification.VerificationMessage;
import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.domain.verification.VerificationType;
import teamdevhub.devhub.port.out.sender.MessageSender;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailMessageSendAdapter implements MessageSender {

    private static final String ENCODING = "UTF-8";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public boolean supports(VerificationTarget target) {
        return target.type() == VerificationType.EMAIL;
    }

    @Override
    public void sendVerification(
            VerificationTarget target,
            VerificationMessage verificationMessage
    ) {
        EmailTemplateType template = EmailTemplateType.EMAIL_VERIFICATION;

        Map<String, Object> variables =
                toVariables(template, verificationMessage);

        send(
                target.value(),
                template,
                variables
        );
    }

    private Map<String, Object> toVariables(
            EmailTemplateType template,
            VerificationMessage message
    ) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("code", message.getCode());
        vars.put("expireTime", template.getExpireTime());
        return vars;
    }

    private void send(
            String email,
            EmailTemplateType template,
            Map<String, Object> variables
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, ENCODING);

            helper.setTo(email);
            helper.setSubject(template.getSubject());
            helper.setText(buildBody(template, variables), true);

            mailSender.send(message);
        } catch (Exception e) {
            throw ExternalServiceException.of(ErrorCode.EMAIL_SEND_FAIL);
        }
    }

    private String buildBody(
            EmailTemplateType template,
            Map<String, Object> variables
    ) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(template.getPath(), context);
    }
}
