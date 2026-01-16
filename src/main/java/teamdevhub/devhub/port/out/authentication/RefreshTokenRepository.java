package teamdevhub.devhub.port.out.authentication;

import teamdevhub.devhub.domain.authentication.vo.RefreshToken;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    RefreshToken findByUserGuid(String userGuid);
    void deleteByUserGuid(String userGuid);
}