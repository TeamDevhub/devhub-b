package teamdevhub.devhub.fake.pure.external;

import teamdevhub.devhub.common.enums.EmailTemplateType;
import teamdevhub.devhub.port.out.mail.EmailNotificationSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FakeEmailNotificationSender implements EmailNotificationSender {

    private final List<SentEmail> sentEmails = new ArrayList<>();

    @Override
    public void send(
            String email,
            EmailTemplateType templateType,
            Map<String, Object> variables
    ) {
        sentEmails.add(
                new SentEmail(email, templateType, variables)
        );
    }

    public List<SentEmail> getSentEmails() {
        return Collections.unmodifiableList(sentEmails);
    }

    public record SentEmail(String email, EmailTemplateType templateType, Map<String, Object> variables) {

            public SentEmail(
                    String email,
                    EmailTemplateType templateType,
                    Map<String, Object> variables
            ) {
                this.email = email;
                this.templateType = templateType;
                this.variables = Map.copyOf(variables);
            }

            public Object getVariable(String key) {
                return variables.get(key);
            }
        }
}