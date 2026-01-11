package teamdevhub.devhub.medium.adapter.out.mail;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import teamdevhub.devhub.adapter.out.exception.ExternalServiceException;
import teamdevhub.devhub.adapter.out.mail.EmailNotificationSendAdapter;
import teamdevhub.devhub.common.enums.EmailTemplateType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmailNotificationSendAdapterTest {

    private JavaMailSender mailSender;
    private EmailNotificationSendAdapter emailNotificationSendAdapter;

    @BeforeEach
    void init() {
        mailSender = mock(JavaMailSender.class);
        SpringTemplateEngine templateEngine = mock(SpringTemplateEngine.class);
        given(templateEngine.process(anyString(), any(Context.class)))
                .willReturn("<html>email body</html>");

        emailNotificationSendAdapter = new EmailNotificationSendAdapter(mailSender, templateEngine);
    }

    @Test
    void 이메일_전송이_요청된다() {
        // given
        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // when
        emailNotificationSendAdapter.send(
                "test@test.com",
                EmailTemplateType.EMAIL_VERIFICATION,
                Map.of("code", "123456")
        );

        // then
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void 메일_전송_실패시_도메인_예외로_변환된다() {
        // given
        given(mailSender.createMimeMessage())
                .willThrow(new MailException("fail") {});

        // then
        assertThatThrownBy(() ->
                // when
                emailNotificationSendAdapter.send(
                        "test@test.com",
                        EmailTemplateType.EMAIL_VERIFICATION,
                        Map.of()
                )
        ).isInstanceOf(ExternalServiceException.class);
    }
}