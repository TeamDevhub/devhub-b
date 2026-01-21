package teamdevhub.devhub.port.out.provider;

import teamdevhub.devhub.domain.auth.vo.token.AccessTokenInfo;
import teamdevhub.devhub.domain.auth.vo.token.RefreshTokenInfo;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;

public interface TokenParseProvider {

    AccessTokenInfo getAccessTokenInfo(String accessToken);
    TempTokenInfo getTempTokenInfo(String tempToken);
    RefreshTokenInfo getRefreshTokenInfo(String refreshToken);
    String removeBearer(String token);
}
