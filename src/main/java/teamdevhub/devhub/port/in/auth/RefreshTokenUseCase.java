package teamdevhub.devhub.port.in.auth;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenUseCase {
    void save(String email, String refreshToken);
    Optional<RefreshTokenEntity> findByEmail(String email);
    void delete(String email);
}
