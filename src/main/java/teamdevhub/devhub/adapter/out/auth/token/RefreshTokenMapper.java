package teamdevhub.devhub.adapter.out.auth.token;

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
