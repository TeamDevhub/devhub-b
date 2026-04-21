package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.core.auth.domain.vo.user.EmailUserCredential;
import teamdevhub.devhub.core.auth.port.out.EmailUserCredentialRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeEmailUserCredentialRepository implements EmailUserCredentialRepository {

    private final Map<String, EmailUserCredential> emailStore = new HashMap<>();
    private final Map<String, EmailUserCredential> userGuidStore = new HashMap<>();

    public void save(EmailUserCredential credential) {
        emailStore.put(credential.email(), credential);
        userGuidStore.put(credential.userGuid(), credential);
    }

    @Override
    public EmailUserCredential findByEmail(String email) {
        return emailStore.get(email);
    }

    @Override
    public EmailUserCredential findByUserGuid(String userGuid) {
        return userGuidStore.get(userGuid);
    }
}
