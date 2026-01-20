package teamdevhub.devhub.port.out.provider;

import teamdevhub.devhub.domain.auth.vo.token.AccessTokenInfo;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.user.UserRole;

public interface TokenIssueProvider {

    String createAccessToken(String userGuid, String email, UserRole userRole);
    String createRefreshToken(String email);
    String createTempToken(String oauthId, SignupStatus signupStatus, VerificationProvider verificationProvider, String email);
    AccessTokenInfo getAccessTokenInfo(String accessToken);
    TempTokenInfo getTempTokenInfo(String tempToken);
    String extractUserGuidFromRefreshToken(String refreshToken);
    String getPrefix();
}