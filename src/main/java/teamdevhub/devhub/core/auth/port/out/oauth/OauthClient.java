package teamdevhub.devhub.core.auth.port.out.oauth;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;

public interface OauthClient {

    boolean supports(VerificationProvider verificationProvider);
    String getAuthorizationUrl();
    OauthUser fetchUser(String authorizationCode);
}