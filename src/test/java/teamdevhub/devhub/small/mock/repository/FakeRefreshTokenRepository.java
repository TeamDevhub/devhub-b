package teamdevhub.devhub.small.mock.repository;

import teamdevhub.devhub.domain.common.vo.auth.RefreshToken;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;

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
}
