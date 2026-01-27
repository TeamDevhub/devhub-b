package teamdevhub.devhub.core.common.provider;

import teamdevhub.devhub.core.auth.domain.vo.token.AccessTokenInfo;
import teamdevhub.devhub.core.auth.domain.vo.token.RefreshTokenInfo;
import teamdevhub.devhub.core.auth.domain.vo.token.TempTokenInfo;

public interface TokenParseProvider {

    AccessTokenInfo getAccessTokenInfo(String accessToken);
    TempTokenInfo getTempTokenInfo(String tempToken);
    RefreshTokenInfo getRefreshTokenInfo(String refreshToken);
    String removeBearer(String token);
}
