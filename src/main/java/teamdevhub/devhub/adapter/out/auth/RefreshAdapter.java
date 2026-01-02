package teamdevhub.devhub.adapter.out.auth;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import teamdevhub.devhub.domain.record.auth.RefreshToken;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public void save(RefreshToken refreshToken) {
        jpaRefreshTokenRepository.findByEmail(refreshToken.email())
                .ifPresentOrElse(
                        refreshTokenEntity -> rotateToken(refreshTokenEntity, refreshToken.token()),
                        () -> jpaRefreshTokenRepository.save(
                                RefreshTokenEntity.of(refreshToken.email(), refreshToken.token())
                        )
                );
    }

    @Override
    public Optional<RefreshToken> findByEmail(String email) {
        return jpaRefreshTokenRepository.findByEmail(email)
                .map(entity -> RefreshToken.of(entity.getEmail(), entity.getToken()));
    }

    @Override
    public void deleteByEmail(String email) {
        jpaRefreshTokenRepository.deleteByEmail(email);
    }

    private void rotateToken(RefreshTokenEntity entity, String newToken) {
        entity.rotate(newToken);
    }
}