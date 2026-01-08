package teamdevhub.devhub.small.mock.repository;

import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.port.out.mail.EmailVerificationRepository;

import java.util.HashMap;
import java.util.List;

public class FakeEmailVerificationRepository implements EmailVerificationRepository {

    private final HashMap<String, EmailCertification> store;
    private final DateTimeProvider dateTimeProvider;

    public FakeEmailVerificationRepository(DateTimeProvider dateTimeProvider) {
        this.store = new HashMap<>();
        this.dateTimeProvider = dateTimeProvider;
    }

    public FakeEmailVerificationRepository(List<EmailCertification> initialData, DateTimeProvider dateTimeProvider) {
        this.store = new HashMap<>();
        this.dateTimeProvider = dateTimeProvider;
        if (initialData != null) {
            for (EmailCertification emailCertification : initialData) {
                store.put(emailCertification.email(), emailCertification);
            }
        }
    }

    @Override
    public EmailCertification findByEmail(String email) {
        return store.get(email);
    }

    @Override
    public boolean existUnexpiredCode(String email) {
        EmailCertification emailCertification = store.get(email);
        if (emailCertification == null) return false;
        return emailCertification.expiredAt().isAfter(dateTimeProvider.now());
    }

    @Override
    public void save(EmailCertification emailCertification) {
        store.put(emailCertification.email(), emailCertification);
    }

    @Override
    public void delete(String email) {
        store.remove(email);
    }
}
