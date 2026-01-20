package teamdevhub.devhub.port.out.provider;

import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.user.UserRole;

public interface TokenIssueProvider {

    String createAccessToken(String userGuid, SignupStatus signupStatus, String email, UserRole userRole);
    String createRefreshToken(String email);
    String createTempToken(String oauthId, SignupStatus signupStatus, VerificationProvider verificationProvider, String email);
    String extractUserGuidFromRefreshToken(String refreshToken);
    String getPrefix();
}