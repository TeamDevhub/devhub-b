package teamdevhub.devhub.port.out.provider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import teamdevhub.devhub.domain.auth.vo.token.AccessTokenInfo;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;

public interface TokenParseProvider {

    Claims parseClaims(String token);
    AccessTokenInfo getAccessTokenInfo(String accessToken);
    TempTokenInfo getTempTokenInfo(String tempToken);
    String resolveToken(HttpServletRequest httpRequest);
    String removeBearer(String token);
}
