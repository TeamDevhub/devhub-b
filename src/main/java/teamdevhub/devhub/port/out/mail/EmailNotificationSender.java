package teamdevhub.devhub.port.out.mail;

public interface EmailNotificationSender {
    void sendEmail(String email, String subject, String code);
}