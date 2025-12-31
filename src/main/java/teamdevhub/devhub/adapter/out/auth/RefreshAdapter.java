package teamdevhub.devhub.adapter.out.auth;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import teamdevhub.devhub.port.out.auth.AuthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshAdapter implements AuthPort {

    private final RefreshTokenRepositoryJpa refreshTokenRepositoryJpa;

    @Override
    public void save(String email, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(email, refreshToken);
        refreshTokenRepositoryJpa.save(refreshTokenEntity);
    }

    @Override
    public Optional<RefreshTokenEntity> findByEmail(String email) {
        return refreshTokenRepositoryJpa.findByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        refreshTokenRepositoryJpa.deleteByEmail(email);
    }
}