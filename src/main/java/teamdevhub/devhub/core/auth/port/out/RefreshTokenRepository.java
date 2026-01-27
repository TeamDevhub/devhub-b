package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.RefreshToken;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);
    RefreshToken findByUserGuid(String userGuid);
    void deleteByUserGuid(String userGuid);
}