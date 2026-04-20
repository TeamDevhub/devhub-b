package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.core.auth.domain.vo.OAuthCredential;
import teamdevhub.devhub.core.auth.port.out.OauthCredentialRepository;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeOauthCredentialRepository implements OauthCredentialRepository {

    private final Map<String, OAuthCredential> store = new HashMap<>();

    private String key(VerificationProvider provider, String oauthId) {
        return provider.name() + "_" + oauthId;
    }

    public void save(OAuthCredential credential) {
        store.put(key(credential.provider(), credential.oauthId()), credential);
    }

    @Override
    public Optional<OAuthCredential> findByProviderAndOauthId(VerificationProvider provider, String oauthId) {
        return Optional.ofNullable(store.get(key(provider, oauthId)));
    }

    @Override
    public Optional<OAuthCredential> findByUserGuid(String userGuid) {
        return store.values().stream()
                .filter(c -> c.userGuid().equals(userGuid))
                .findFirst();
    }
}
