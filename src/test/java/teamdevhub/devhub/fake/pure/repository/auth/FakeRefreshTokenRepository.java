package teamdevhub.devhub.fake.pure.repository.auth;

import teamdevhub.devhub.core.auth.domain.RefreshToken;
import teamdevhub.devhub.core.auth.port.out.RefreshTokenRepository;

import java.util.HashMap;

public class FakeRefreshTokenRepository implements RefreshTokenRepository {

    private final HashMap<String, RefreshToken> store = new HashMap<>();

    @Override
    public void save(RefreshToken refreshToken) {
        store.put(refreshToken.userGuid(), refreshToken);
    }

    @Override
    public RefreshToken findByUserGuid(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public void deleteByUserGuid(String userGuid) {
        store.remove(userGuid);
    }

    public void givenRefreshToken(RefreshToken refreshToken) {
        store.put(refreshToken.userGuid(), refreshToken);
    }
}
