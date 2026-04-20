package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.core.auth.domain.vo.EmailCredential;
import teamdevhub.devhub.core.auth.port.out.EmailCredentialRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeEmailCredentialRepository implements EmailCredentialRepository {

    private final Map<String, EmailCredential> emailStore = new HashMap<>();
    private final Map<String, EmailCredential> userGuidStore = new HashMap<>();

    public void save(EmailCredential credential) {
        emailStore.put(credential.email(), credential);
        userGuidStore.put(credential.userGuid(), credential);
    }

    @Override
    public EmailCredential findByEmail(String email) {
        return emailStore.get(email);
    }

    @Override
    public EmailCredential findByUserGuid(String userGuid) {
        return userGuidStore.get(userGuid);
    }
}
