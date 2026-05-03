package teamdevhub.devhub.core.auth.port.out.oauth;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;

public interface OauthClient {

    boolean supports(VerificationProvider verificationProvider);
    String getAuthorizationUrl(String state);
    OauthUser fetchUser(String authorizationCode);
}