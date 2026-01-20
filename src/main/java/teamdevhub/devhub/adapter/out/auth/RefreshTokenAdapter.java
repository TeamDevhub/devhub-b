package teamdevhub.devhub.adapter.out.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.auth.JpaRefreshTokenRepository;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.auth.vo.token.RefreshTokenInfo;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;

@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public void save(RefreshTokenInfo refreshTokenInfo) {
        jpaRefreshTokenRepository.findByUserGuid(refreshTokenInfo.userGuid())
                .ifPresentOrElse(
                        refreshTokenEntity -> refreshTokenEntity.rotate(refreshTokenInfo.token()),
                        () -> jpaRefreshTokenRepository.save(RefreshTokenEntity.of(refreshTokenInfo.userGuid(), refreshTokenInfo.token()))
                );
    }

    @Override
    public RefreshTokenInfo findByUserGuid(String userGuid) {
        return jpaRefreshTokenRepository.findByUserGuid(userGuid)
                .map(refreshTokenEntity -> RefreshTokenInfo.of(refreshTokenEntity.getUserGuid(), refreshTokenEntity.getToken()))
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.REFRESH_TOKEN_INVALID));
    }

    @Override
    public void deleteByUserGuid(String userGuid) {
        jpaRefreshTokenRepository.deleteByUserGuid(userGuid);
    }
}