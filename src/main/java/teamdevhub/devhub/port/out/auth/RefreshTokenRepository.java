package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.domain.auth.vo.token.RefreshTokenInfo;

public interface RefreshTokenRepository {

    void save(RefreshTokenInfo refreshTokenInfo);
    RefreshTokenInfo findByUserGuid(String userGuid);
    void deleteByUserGuid(String userGuid);
}