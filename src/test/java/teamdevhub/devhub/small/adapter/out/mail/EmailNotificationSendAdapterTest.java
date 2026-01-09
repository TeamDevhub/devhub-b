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
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        adapter = new EmailNotificationSendAdapter(fakeMailSender, templateEngine);
    }

    @Test
    void send_를_호출하면_메일이_전송된다() throws MessagingException {
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