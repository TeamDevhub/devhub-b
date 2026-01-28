package teamdevhub.devhub.core.auth.port.out.token;

import teamdevhub.devhub.core.auth.application.service.token.RefreshToken;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);
    RefreshToken findByUserGuid(String userGuid);
    void deleteByUserGuid(String userGuid);
}