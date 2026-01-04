package teamdevhub.devhub.port.out.mail;

import teamdevhub.devhub.domain.common.record.mail.EmailCertification;

public interface EmailCertificationRepository {
    boolean hasUnexpiredCode(String email);
    void save(EmailCertification emailCertification);
    boolean verify(String email, String code);
    boolean isVerified(String email);
    void delete(String email);
}