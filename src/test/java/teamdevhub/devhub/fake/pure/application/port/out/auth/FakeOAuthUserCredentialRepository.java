package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.core.auth.domain.vo.user.OAuthUserCredential;
import teamdevhub.devhub.core.auth.port.out.OAuthUserCredentialRepository;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeOAuthUserCredentialRepository implements OAuthUserCredentialRepository {

    private final Map<String, OAuthUserCredential> store = new HashMap<>();

    private String key(VerificationProvider provider, String oauthId) {
        return provider.name() + "_" + oauthId;
    }

    public void save(OAuthUserCredential credential) {
        store.put(key(credential.provider(), credential.oauthId()), credential);
    }

    @Override
    public Optional<OAuthUserCredential> findByProviderAndOauthId(VerificationProvider provider, String oauthId) {
        return Optional.ofNullable(store.get(key(provider, oauthId)));
    }

    @Override
    public Optional<OAuthUserCredential> findByUserGuid(String userGuid) {
        return store.values().stream()
                .filter(c -> c.userGuid().equals(userGuid))
                .findFirst();
    }
}
