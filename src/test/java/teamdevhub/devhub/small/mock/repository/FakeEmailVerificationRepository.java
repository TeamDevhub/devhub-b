package teamdevhub.devhub.small.mock.repository;

import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.domain.mail.EmailVerification;
import teamdevhub.devhub.port.out.mail.EmailVerificationRepository;

import java.util.HashMap;
import java.util.List;

public class FakeEmailVerificationRepository implements EmailVerificationRepository {

    private final HashMap<String, EmailVerification> store;
    private final DateTimeProvider dateTimeProvider;

    public FakeEmailVerificationRepository(DateTimeProvider dateTimeProvider) {
        this.store = new HashMap<>();
        this.dateTimeProvider = dateTimeProvider;
    }

    public FakeEmailVerificationRepository(List<EmailVerification> initialData, DateTimeProvider dateTimeProvider) {
        this.store = new HashMap<>();
        this.dateTimeProvider = dateTimeProvider;
        if (initialData != null) {
            for (EmailVerification emailVerification : initialData) {
                store.put(emailVerification.getEmail(), emailVerification);
            }
        }
    }

    @Override
    public EmailVerification findByEmail(String email) {
        return store.get(email);
    }

    @Override
    public boolean existUnexpiredCode(String email) {
        EmailVerification emailVerification = store.get(email);
        if (emailVerification == null) return false;
        return emailVerification.getExpiredAt().isAfter(dateTimeProvider.now());
    }

    @Override
    public void save(EmailVerification emailVerification) {
        store.put(emailVerification.getEmail(), emailVerification);
    }

    @Override
    public void delete(String email) {
        store.remove(email);
    }
}
