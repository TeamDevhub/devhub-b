package teamdevhub.devhub.port.out.mail;

import teamdevhub.devhub.domain.mail.EmailVerification;

public interface EmailVerificationRepository {
    EmailVerification getByEmail(String email);
    boolean existUnexpiredCode(String email);
    void save(EmailVerification emailVerification);
    void delete(String email);
}