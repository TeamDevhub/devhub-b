package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.core.auth.domain.vo.user.EmailUserCredential;
import teamdevhub.devhub.core.auth.port.out.EmailUserCredentialRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeEmailUserCredentialRepository implements EmailUserCredentialRepository {

    private final Map<String, EmailUserCredential> emailStore = new HashMap<>();
    private final Map<String, EmailUserCredential> userGuidStore = new HashMap<>();

    public void save(EmailUserCredential credential) {
        emailStore.put(credential.email(), credential);
        userGuidStore.put(credential.userGuid(), credential);
    }

    @Override
    public Optional<EmailUserCredential> findByEmail(String email) {
        return Optional.ofNullable(emailStore.get(email));
    }

    @Override
    public Optional<EmailUserCredential> findByUserGuid(String userGuid) {
        return Optional.ofNullable(userGuidStore.get(userGuid));
    }
}