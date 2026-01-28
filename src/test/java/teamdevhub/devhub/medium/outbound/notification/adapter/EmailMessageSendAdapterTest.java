package teamdevhub.devhub.medium.outbound.notification.adapter;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;
import teamdevhub.devhub.shared.exception.ExternalServiceException;
import teamdevhub.devhub.outbound.notification.adapter.EmailMessageSendAdapter;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_CODE;

public class EmailMessageSendAdapterTest {

    private EmailMessageSendAdapter emailMessageSendAdapter;

    private JavaMailSender mailSender;
    private SpringTemplateEngine templateEngine;

    @BeforeEach
    void init() {
        mailSender = mock(JavaMailSender.class);
        templateEngine = mock(SpringTemplateEngine.class);

        emailMessageSendAdapter = new EmailMessageSendAdapter(mailSender, templateEngine);
    }

    @Test
    @DisplayName("VerificationType_이_EMAIL_이면_supports_는_true_를_반환한다")
    void supportsReturnsTrueForEmail() {
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL);

        assertThat(emailMessageSendAdapter.supports(verificationTarget)).isTrue();
    }

    @Test
    @DisplayName("VerificationType_이_SMS_이면_supports_는_false_를_반환한다")
    void supportsReturnsFalseForSMS() {
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.SMS, "01012345678");

        assertThat(emailMessageSendAdapter.supports(verificationTarget)).isFalse();
    }

    @Test
    @DisplayName("메일_전송_호출시_JavaMailSender_가_호출된다")
    void sendVerificationCallsMailSender() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, null);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any())).thenReturn("<html>test</html>");

        // when
        emailMessageSendAdapter.sendVerification(verificationTarget, verificationMessage);

        // then
        verify(mailSender, times(1)).send(mimeMessage);
        verify(templateEngine, times(1)).process(anyString(), any());
    }

    @Test
    @DisplayName("지원하지_않는_VerificationType_으로_send_하면_예외가_발생한다")
    void sendVerificationThrowsExternalServiceException() {
        // given
        VerificationTarget target = VerificationTarget.of(VerificationType.OTP, "010-1234-5678");
        VerificationMessage message = new VerificationMessage("123456", null);

        // when, then
        assertThatThrownBy(() -> emailMessageSendAdapter.sendVerification(target, message))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("발송이 실패했습니다");
    }
}
