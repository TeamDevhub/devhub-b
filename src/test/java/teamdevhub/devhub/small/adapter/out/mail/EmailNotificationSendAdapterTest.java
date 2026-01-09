package teamdevhub.devhub.small.adapter.out.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.spring6.SpringTemplateEngine;
import teamdevhub.devhub.adapter.out.mail.EmailNotificationSendAdapter;
import teamdevhub.devhub.common.enums.EmailTemplateType;
import teamdevhub.devhub.small.mock.infrastructure.FakeJavaMailSender;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

class EmailNotificationSendAdapterTest {

    private EmailNotificationSendAdapter adapter;
    private FakeJavaMailSender fakeMailSender;

    @BeforeEach
    void init() {
        fakeMailSender = new FakeJavaMailSender();
        SpringTemplateEngine templateEngine = new SpringTemplateEngine(); // 실제 프로세스 호출 안 할 거면 OK
        adapter = new EmailNotificationSendAdapter(fakeMailSender, templateEngine);
    }

    @Test
    void send_호출시_메일_전송됨() throws MessagingException {
        // given
        EmailTemplateType templateType = EmailTemplateType.EMAIL_VERIFICATION;
        Map<String, Object> variables = Map.of(EMAIL_CODE, "5분");

        // when
        adapter.send(TEST_EMAIL, templateType, variables);

        // then
        MimeMessage sentMessage = fakeMailSender.getLastSentMessage();
        assertThat(sentMessage).isNotNull();
        assertThat(sentMessage.getAllRecipients()[0].toString()).isEqualTo(TEST_EMAIL);
    }
}