package teamdevhub.devhub.core.auth.port.out.token;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

public interface TokenIssueProvider {

    String createAccessToken(String userGuid, String email, UserRole userRole);
    String createRefreshToken(String email);
    String createTempToken(String oauthId, VerificationProvider verificationProvider, String email);
}