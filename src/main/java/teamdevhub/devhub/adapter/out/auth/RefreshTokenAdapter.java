package teamdevhub.devhub.adapter.out.auth;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import teamdevhub.devhub.adapter.out.auth.persist.JpaRefreshTokenRepository;
import teamdevhub.devhub.adapter.out.common.exception.DataAccessException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.common.record.auth.RefreshToken;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public void save(RefreshToken refreshToken) {
        jpaRefreshTokenRepository.findByEmail(refreshToken.email())
                .ifPresentOrElse(
                        refreshTokenEntity -> refreshTokenEntity.rotate(refreshToken.token()),
                        () -> jpaRefreshTokenRepository.save(RefreshTokenEntity.of(refreshToken.email(), refreshToken.token()))
                );
    }

    @Override
    public RefreshToken findByEmail(String email) {
        return jpaRefreshTokenRepository.findByEmail(email)
                .map(refreshTokenEntity -> RefreshToken.of(refreshTokenEntity.getEmail(), refreshTokenEntity.getToken()))
                .orElseThrow(() -> DataAccessException.of(ErrorCode.REFRESH_TOKEN_INVALID));
    }

    @Override
    public void deleteByEmail(String email) {
        jpaRefreshTokenRepository.deleteByEmail(email);
    }
}