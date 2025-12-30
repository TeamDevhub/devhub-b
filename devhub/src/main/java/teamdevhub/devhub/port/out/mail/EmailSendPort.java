package teamdevhub.devhub.port.out.mail;

public interface EmailSendPort {
    void sendEmail(String email, String subject, String code);
}