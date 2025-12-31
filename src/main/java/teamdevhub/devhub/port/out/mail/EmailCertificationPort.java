package teamdevhub.devhub.port.out.mail;

import java.time.Duration;

public interface EmailCertificationPort {
    void save(String email, String code, Duration ttl);
    boolean existsValidCode(String email);
    boolean verify(String email, String code);
    boolean isVerified(String email);
    void delete(String email);
}