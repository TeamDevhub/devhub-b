package teamdevhub.devhub.core.auth.port.out.token;

import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.VerificationProvider;

public interface TokenIssueProvider {

    String createAccessToken(AuthenticatedUser authenticatedUser);
    String createRefreshToken(String email);
    String createTempToken(String oAuthId, VerificationProvider verificationProvider, String email);
}