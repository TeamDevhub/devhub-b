package teamdevhub.devhub.port.out.provider;

import teamdevhub.devhub.domain.user.UserRole;

public interface TokenIssueProvider {
    String createAccessToken(String userGuid, String email, UserRole userRole);
    String createRefreshToken(String email);
    String extractUserGuidFromRefreshToken(String refreshToken);
    String getPrefix();
}