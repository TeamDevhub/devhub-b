package teamdevhub.devhub.port.out.provider;

import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.user.vo.UserRole;

public interface TokenIssueProvider {

    String createAccessToken(String userGuid, String email, UserRole userRole);
    String createRefreshToken(String email);
    String createTempToken(String oauthId, VerificationProvider verificationProvider, String email);
}