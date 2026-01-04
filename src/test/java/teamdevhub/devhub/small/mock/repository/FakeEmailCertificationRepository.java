package teamdevhub.devhub.small.mock.repository;

import teamdevhub.devhub.domain.common.record.mail.EmailCertification;
import teamdevhub.devhub.port.out.common.DateTimeProvider;
import teamdevhub.devhub.port.out.mail.EmailCertificationRepository;

import java.util.HashMap;
import java.util.List;

public class FakeEmailCertificationRepository implements EmailCertificationRepository {

    private final HashMap<String, EmailCertification> store;
    private final DateTimeProvider dateTimeProvider;

    public FakeEmailCertificationRepository(DateTimeProvider dateTimeProvider) {
        this.store = new HashMap<>();
        this.dateTimeProvider = dateTimeProvider;
    }

    public FakeEmailCertificationRepository(List<EmailCertification> initialData, DateTimeProvider dateTimeProvider) {
        this.store = new HashMap<>();
        this.dateTimeProvider = dateTimeProvider;
        if (initialData != null) {
            for (EmailCertification emailCertification : initialData) {
                store.put(emailCertification.email(), emailCertification);
            }
        }
    }

    @Override
    public boolean hasUnexpiredCode(String email) {
        EmailCertification emailCertification = store.get(email);
        if (emailCertification == null) return false;
        return emailCertification.expiredAt().isAfter(dateTimeProvider.now());
    }

    @Override
    public void save(EmailCertification emailCertification) {
        store.put(emailCertification.email(), emailCertification);
    }

    @Override
    public boolean verify(String email, String code) {
        EmailCertification emailCertification = store.get(email);
        if (emailCertification == null) return false;

        if (emailCertification.code().equals(code) && emailCertification.expiredAt().isAfter(dateTimeProvider.now())) {
            EmailCertification verifiedCert = EmailCertification.of(emailCertification.email(),
                    emailCertification.code(),
                    dateTimeProvider.now(),
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
