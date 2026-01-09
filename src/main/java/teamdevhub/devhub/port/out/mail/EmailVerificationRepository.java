package teamdevhub.devhub.port.out.mail;

import teamdevhub.devhub.domain.mail.EmailVerification;

public interface EmailVerificationRepository {
    EmailVerification findByEmail(String email);
    void save(EmailVerification emailVerification);
    boolean existUnexpiredCode(String email);
    void delete(String email);
}