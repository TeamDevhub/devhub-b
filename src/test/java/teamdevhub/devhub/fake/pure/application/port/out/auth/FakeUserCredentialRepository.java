package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeUserCredentialRepository implements UserCredentialRepository {

    private final Map<String, UserCredential> emailByGuidStore = new HashMap<>();
    private final Map<String, UserCredential> emailByEmailStore = new HashMap<>();
    private final Map<String, UserCredential> oauthStore = new HashMap<>();

    @Override
    public Optional<UserCredential> findEmailUserCredentialByUserGuid(String userGuid) {
        return Optional.ofNullable(emailByGuidStore.get(userGuid));
    }

    @Override
    public Optional<UserCredential> findEmailUserCredentialByEmail(String email) {
        return Optional.ofNullable(emailByEmailStore.get(email));
    }

    @Override
    public Optional<UserCredential> findOAuthUserCredentialByOAuth(VerificationProvider verificationProvider, String oauthId) {
        String key = verificationProvider.name() + ":" + oauthId;
        return Optional.ofNullable(oauthStore.get(key));
    }

    @Override
    public void saveEmailUserCredential(UserCredential userCredential, String encryptedPassword) {
        emailByGuidStore.put(userCredential.userGuid(), userCredential);
        emailByEmailStore.put(userCredential.loginId(), userCredential);
    }

    @Override
    public void saveOAuthUserCredential(UserCredential userCredential, VerificationProvider verificationProvider, String oauthId) {
        String key = verificationProvider.name() + ":" + oauthId;
        oauthStore.put(key, userCredential);
    }
}
