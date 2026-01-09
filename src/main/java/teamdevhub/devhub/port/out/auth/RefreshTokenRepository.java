package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.domain.common.vo.auth.RefreshToken;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    RefreshToken findByUserGuid(String userGuid);
    void deleteByUserGuid(String userGuid);
}