package teamdevhub.devhub.small.mock.repository;

import teamdevhub.devhub.domain.record.auth.RefreshToken;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;

import java.util.HashMap;
import java.util.Optional;

public class FakeRefreshTokenRepository implements RefreshTokenRepository {

    private final HashMap<String, RefreshToken> store = new HashMap<>();

    @Override
    public void save(RefreshToken refreshToken) {
        store.put(refreshToken.email(), refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByEmail(String email) {
        return Optional.ofNullable(store.get(email));
    }

    @Override
    public void deleteByEmail(String email) {
        store.remove(email);
    }
}
