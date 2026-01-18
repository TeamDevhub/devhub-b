package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.domain.auth.vo.RefreshToken;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);
    RefreshToken findByUserGuid(String userGuid);
    void deleteByUserGuid(String userGuid);
}