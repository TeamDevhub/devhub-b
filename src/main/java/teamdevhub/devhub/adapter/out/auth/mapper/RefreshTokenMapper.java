package teamdevhub.devhub.adapter.out.auth.mapper;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import teamdevhub.devhub.domain.auth.RefreshToken;

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
