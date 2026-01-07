package teamdevhub.devhub.port.out.provider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import teamdevhub.devhub.domain.user.UserRole;

public interface TokenProvider {
    String createAccessToken(String userGuid, String email, UserRole userRole);
    String createRefreshToken(String email);
    Claims parseClaims(String token);
    String extractEmailFromRefreshToken(String refreshToken);
    String resolveToken(HttpServletRequest httpRequest);
    String removeBearer(String token);
    String getPrefix();
}