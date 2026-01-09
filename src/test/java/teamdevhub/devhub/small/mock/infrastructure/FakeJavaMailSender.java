package teamdevhub.devhub.small.mock.infrastructure;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.InputStream;

public class FakeJavaMailSender implements JavaMailSender {

    boolean sendCalled = false;
    private MimeMessage lastSentMessage;

    @Override
    public MimeMessage createMimeMessage() {
        return new MimeMessage((Session) null);
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;
    }

    @Override
    public void send(MimeMessage mimeMessage) {
        this.sendCalled = true;
        this.lastSentMessage = mimeMessage;
    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }

    public MimeMessage getLastSentMessage() {
        return lastSentMessage;
    }
}
