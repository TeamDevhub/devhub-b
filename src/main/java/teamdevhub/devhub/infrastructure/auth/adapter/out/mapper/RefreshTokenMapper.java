package teamdevhub.devhub.infrastructure.auth.adapter.out.mapper;

import teamdevhub.devhub.infrastructure.auth.adapter.out.entity.RefreshTokenEntity;
import teamdevhub.devhub.core.auth.domain.RefreshToken;

public class RefreshTokenMapper {

    public static RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        return RefreshTokenEntity.of(
                refreshToken.userGuid(),
                refreshToken.token()
        );
    }

    public static RefreshToken toDomain(RefreshTokenEntity refreshTokenEntity) {
        return RefreshToken.of(
                refreshTokenEntity.getUserGuid(),
                refreshTokenEntity.getToken()
        );
    }
}
