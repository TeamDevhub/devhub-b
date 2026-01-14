package teamdevhub.devhub.medium.adapter.out.mail;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static teamdevhub.devhub.constant.UserTestConstant.*;

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
    @DisplayName("이메일_전송이_요청된다")
    void requestEmailSend() {
        // given
        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // when
        emailNotificationSendAdapter.send(
                TEST_EMAIL_1,
                EmailTemplateType.EMAIL_VERIFICATION,
                Map.of("code", EMAIL_CODE)
        );

        // then
        verify(mailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("메일_전송_실패시_ExternalServiceException_으로_변환된다")
    void convertToExternalServiceExceptionIfEmailSendFails() {
        // given
        given(mailSender.createMimeMessage())
                .willThrow(new MailException("fail") {});

        // then
        assertThatThrownBy(
                // when
                () -> emailNotificationSendAdapter.send(
                        TEST_EMAIL_1,
                        EmailTemplateType.EMAIL_VERIFICATION,
                        Map.of())
        ).isInstanceOf(ExternalServiceException.class);
    }
}