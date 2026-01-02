package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import teamdevhub.devhub.domain.record.auth.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    Optional<RefreshToken> findByEmail(String email);
    void deleteByEmail(String email);
}