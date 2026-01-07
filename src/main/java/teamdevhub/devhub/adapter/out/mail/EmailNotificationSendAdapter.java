package teamdevhub.devhub.adapter.out.mail;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import teamdevhub.devhub.adapter.out.common.exception.ExternalServiceException;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.port.out.mail.EmailNotificationSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationSendAdapter implements EmailNotificationSender {

    private static final String ENCODING = "UTF-8";
    private static final String EMAIL_CERTIFICATION_TEMPLATE_CODE_NAME = "code";
    private static final String EMAIL_CERTIFICATION_TEMPLATE_EXPIRE_TIME_NAME = "expireTime";
    private static final String EMAIL_CERTIFICATION_TEMPLATE_EXPIRE_TIME_VALUE = "5분";
    private static final String EMAIL_CERTIFICATION_TEMPLATE_PATH_NAME = "email/certification";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    @Async
    public void sendEmail(String email, String subject, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,ENCODING);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(buildBody(code), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw ExternalServiceException.of(ErrorCodeEnum.EMAIL_SEND_FAIL);
        }
    }

    private String buildBody(String code) {
        Context context = new Context();
        context.setVariable(EMAIL_CERTIFICATION_TEMPLATE_CODE_NAME, code);
        context.setVariable(EMAIL_CERTIFICATION_TEMPLATE_EXPIRE_TIME_NAME, EMAIL_CERTIFICATION_TEMPLATE_EXPIRE_TIME_VALUE);
        return templateEngine.process(EMAIL_CERTIFICATION_TEMPLATE_PATH_NAME, context);
    }
}
