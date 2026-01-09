package teamdevhub.devhub.adapter.out.mail;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import teamdevhub.devhub.adapter.out.exception.ExternalServiceException;
import teamdevhub.devhub.common.enums.EmailTemplateType;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.port.out.mail.EmailNotificationSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailNotificationSendAdapter implements EmailNotificationSender {

    private static final String ENCODING = "UTF-8";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    @Async
    public void send(String email, EmailTemplateType templateType, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    ENCODING
            );

            helper.setTo(email);
            helper.setSubject(templateType.getSubject());
            helper.setText(buildBody(templateType, variables), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw ExternalServiceException.of(ErrorCode.EMAIL_SEND_FAIL);
        }
    }

    private String buildBody(EmailTemplateType templateType, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateType.getPath(), context);
    }
}
