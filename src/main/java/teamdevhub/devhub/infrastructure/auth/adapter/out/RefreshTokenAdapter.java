package teamdevhub.devhub.infrastructure.auth.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.infrastructure.auth.adapter.out.mapper.RefreshTokenMapper;
import teamdevhub.devhub.shared.exception.AdapterDataException;
import teamdevhub.devhub.infrastructure.auth.adapter.out.persistence.JpaRefreshTokenRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.domain.RefreshToken;
import teamdevhub.devhub.core.auth.port.out.RefreshTokenRepository;

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