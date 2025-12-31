package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenPort {
    void save(String email, String refreshToken);
    Optional<RefreshTokenEntity> findByEmail(String email);
    void deleteByEmail(String email);
}