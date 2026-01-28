package teamdevhub.devhub.core.auth.port.out.token;

import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.AccessTokenInfo;
import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.TempTokenInfo;

public interface TokenParseProvider {

    AccessTokenInfo getAccessTokenInfo(String accessToken);
    TempTokenInfo getTempTokenInfo(String tempToken);
    String getRefreshTokenInfo(String refreshToken);
    String removeBearer(String token);
}
