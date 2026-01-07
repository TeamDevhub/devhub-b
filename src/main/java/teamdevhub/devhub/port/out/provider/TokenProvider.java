package teamdevhub.devhub.port.out.provider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.domain.user.UserRole;

public interface TokenProvider {
    String createRefreshToken(String email);
    JwtStatusEnum validateToken(String token);
    Claims getUserInfo(String token);
    String getEmailFromRefreshToken(String refreshToken);
    String substringHeaderToken(String token);
    String getTokenFromHeader(HttpServletRequest req);
    String createAccessToken(String userGuid, String email, UserRole userRole);
    String getPrefix();
}