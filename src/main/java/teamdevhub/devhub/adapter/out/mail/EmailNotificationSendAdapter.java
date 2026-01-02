package teamdevhub.devhub.adapter.out.mail;

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

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendEmail(String email, String subject, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    "UTF-8");

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(buildBody(code), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 발송 실패", e);
        }
    }

    private String buildBody(String code) {
        return """
            <h2>이메일 인증 코드</h2>
            <p>아래 인증 코드를 입력해주세요.</p>
            <h1>%s</h1>
            <p>유효시간은 5분입니다.</p>
            """.formatted(code);
    }
}
