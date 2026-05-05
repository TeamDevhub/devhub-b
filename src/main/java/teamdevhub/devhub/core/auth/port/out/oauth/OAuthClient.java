package teamdevhub.devhub.core.auth.port.out.oauth;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OAuthUser;

public interface OAuthClient {

    boolean supports(VerificationProvider verificationProvider);
    String getAuthorizationUrl(String state);
    OAuthUser fetchUser(String authorizationCode);
}