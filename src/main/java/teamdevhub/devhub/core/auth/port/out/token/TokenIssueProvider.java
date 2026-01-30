package teamdevhub.devhub.core.auth.port.out.token;

import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.VerificationProvider;

public interface TokenIssueProvider {

    String createAccessToken(AuthenticatedUser authenticatedUser);
    String createRefreshToken(String email);
    String createTempToken(String oauthId, VerificationProvider verificationProvider, String email);
}