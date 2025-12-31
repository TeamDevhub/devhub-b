package teamdevhub.devhub.port.out.common;

import io.jsonwebtoken.Claims;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.domain.user.UserRole;

public interface TokenProvider {
    JwtStatusEnum validateToken(String token);

    Claims getUserInfo(String token);

    String getEmailFromRefreshToken(String refreshToken);
    String createAccessToken(String email, UserRole userRole);
}