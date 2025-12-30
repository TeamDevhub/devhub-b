package teamdevhub.devhub.port.out.common;

import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.domain.user.UserRole;

public interface AuthTokenPort {
    JwtStatusEnum validateToken(String token);
    String getEmailFromRefreshToken(String refreshToken);
    String createAccessToken(String email, UserRole userRole);
}