package teamdevhub.devhub.port.out.mail;

import teamdevhub.devhub.domain.common.record.mail.EmailCertification;

public interface EmailCertificationRepository {
    EmailCertification findByEmail(String email);
    boolean existUnexpiredCode(String email);
    void save(EmailCertification emailCertification);
    void delete(String email);
}