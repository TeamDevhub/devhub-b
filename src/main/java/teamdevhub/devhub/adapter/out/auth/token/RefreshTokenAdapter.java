package teamdevhub.devhub.adapter.out.auth.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.auth.JpaRefreshTokenRepository;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.auth.RefreshToken;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;

@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public void save(RefreshToken refreshToken) {
        jpaRefreshTokenRepository.findByUserGuid(refreshToken.userGuid())
                .ifPresentOrElse(
                        refreshTokenEntity -> refreshTokenEntity.rotate(refreshToken.token()),
                        () -> jpaRefreshTokenRepository.save(
                                RefreshTokenMapper.toEntity(refreshToken)
                        )
                );
    }

    @Override
    public RefreshToken findByUserGuid(String userGuid) {
        return jpaRefreshTokenRepository.findByUserGuid(userGuid)
                .map(refreshTokenEntity -> RefreshToken.of(refreshTokenEntity.getUserGuid(), refreshTokenEntity.getToken()))
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.REFRESH_TOKEN_INVALID));
    }

    @Override
    public void deleteByUserGuid(String userGuid) {
        jpaRefreshTokenRepository.deleteByUserGuid(userGuid);
    }
}