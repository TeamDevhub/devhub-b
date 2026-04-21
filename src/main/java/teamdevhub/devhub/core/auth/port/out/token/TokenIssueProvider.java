package teamdevhub.devhub.core.auth.port.out.token;

import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.shared.enums.VerificationProvider;

public interface TokenIssueProvider {

    String createAccessToken(UserCredential userCredential);
    String createRefreshToken(String email);
    String createTempToken(String oauthId, VerificationProvider verificationProvider, String email);
}