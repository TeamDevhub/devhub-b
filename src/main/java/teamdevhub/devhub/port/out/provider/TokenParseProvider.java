package teamdevhub.devhub.port.out.provider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenParseProvider {
    Claims parseClaims(String token);
    String resolveToken(HttpServletRequest httpRequest);
    String removeBearer(String token);
}
