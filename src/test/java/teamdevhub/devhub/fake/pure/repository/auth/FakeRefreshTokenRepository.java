package teamdevhub.devhub.fake.pure.repository.auth;

import teamdevhub.devhub.domain.auth.vo.token.RefreshTokenInfo;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;

import java.util.HashMap;

public class FakeRefreshTokenRepository implements RefreshTokenRepository {

    private final HashMap<String, RefreshTokenInfo> store = new HashMap<>();

    @Override
    public void save(RefreshTokenInfo refreshTokenInfo) {
        store.put(refreshTokenInfo.userGuid(), refreshTokenInfo);
    }

    @Override
    public RefreshTokenInfo findByUserGuid(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public void deleteByUserGuid(String userGuid) {
        store.remove(userGuid);
    }
}
