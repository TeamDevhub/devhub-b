package teamdevhub.devhub.small.mock.infrastructure;

import teamdevhub.devhub.port.out.mail.EmailNotificationSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeEmailNotificationSender implements EmailNotificationSender {

    private final List<SentEmail> sentEmails = new ArrayList<>();

    @Override
    public void sendEmail(String email, String subject, String code) {
        sentEmails.add(new SentEmail(email, subject, code));
    }

    public List<SentEmail> getSentEmails() {
        return Collections.unmodifiableList(sentEmails);
    }

    public void clear() {
        sentEmails.clear();
    }

    public static class SentEmail {
        private final String email;
        private final String subject;
        private final String code;

        public SentEmail(String email, String subject, String code) {
            this.email = email;
            this.subject = subject;
            this.code = code;
        }

        public String getEmail() {
            return email;
        }

        public String getSubject() {
            return subject;
        }

        public String getCode() {
            return code;
        }
    }
}
