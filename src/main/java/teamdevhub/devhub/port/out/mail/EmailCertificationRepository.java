package teamdevhub.devhub.port.out.mail;

import teamdevhub.devhub.domain.record.mail.EmailCertification;

public interface EmailCertificationRepository {
    void save(EmailCertification emailCertification);
    boolean existsValidCode(String email);
    boolean verify(String email, String code);
    boolean isVerified(String email);
    void delete(String email);
}