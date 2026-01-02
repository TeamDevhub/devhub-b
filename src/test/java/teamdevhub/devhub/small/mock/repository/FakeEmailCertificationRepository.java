package teamdevhub.devhub.small.mock.repository;

import teamdevhub.devhub.domain.record.mail.EmailCertification;
import teamdevhub.devhub.port.out.mail.EmailCertificationRepository;

import java.time.LocalDateTime;
import java.util.HashMap;

public class FakeEmailCertificationRepository implements EmailCertificationRepository {

    private final HashMap<String, EmailCertification> store = new HashMap<>();

    @Override
    public void save(EmailCertification emailCertification) {
        store.put(emailCertification.email(), emailCertification);
    }

    @Override
    public boolean existsValidCode(String email) {
        EmailCertification emailCertification = store.get(email);
        if (emailCertification == null) return false;
        return emailCertification.expiredAt().isAfter(java.time.LocalDateTime.now());
    }

    @Override
    public boolean verify(String email, String code) {
        EmailCertification emailCertification = store.get(email);
        if (emailCertification == null) return false;

        if (emailCertification.code().equals(code) && emailCertification.expiredAt().isAfter(java.time.LocalDateTime.now())) {
            EmailCertification verifiedCert = EmailCertification.of(emailCertification.email(),
                    emailCertification.code(),
                    LocalDateTime.now(),
                    emailCertification.expiredAt());
            store.put(email, verifiedCert);
            return true;
        }
        return false;
    }

    @Override
    public boolean isVerified(String email) {
        EmailCertification emailCertification = store.get(email);
        return emailCertification != null && emailCertification.verifiedAt() != null;
    }

    @Override
    public void delete(String email) {
        store.remove(email);
    }
}
