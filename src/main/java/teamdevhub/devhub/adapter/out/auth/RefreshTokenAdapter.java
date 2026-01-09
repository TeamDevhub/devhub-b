package teamdevhub.devhub.adapter.out.auth;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import teamdevhub.devhub.adapter.out.auth.persistence.JpaRefreshTokenRepository;
import teamdevhub.devhub.adapter.out.exception.DataAccessException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.vo.auth.RefreshToken;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public void save(RefreshToken refreshToken) {
        jpaRefreshTokenRepository.findByUserGuid(refreshToken.userGuid())
                .ifPresentOrElse(
                        refreshTokenEntity -> refreshTokenEntity.rotate(refreshToken.token()),
                        () -> jpaRefreshTokenRepository.save(RefreshTokenEntity.of(refreshToken.userGuid(), refreshToken.token()))
                );
    }

    @Override
    public RefreshToken findByUserGuid(String userGuid) {
        return jpaRefreshTokenRepository.findByUserGuid(userGuid)
                .map(refreshTokenEntity -> RefreshToken.of(refreshTokenEntity.getUserGuid(), refreshTokenEntity.getToken()))
                .orElseThrow(() -> DataAccessException.of(ErrorCode.REFRESH_TOKEN_INVALID));
    }

    @Override
    public void deleteByUserGuid(String userGuid) {
        jpaRefreshTokenRepository.deleteByUserGuid(userGuid);
    }
}