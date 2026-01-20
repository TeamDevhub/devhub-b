package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;

public interface OauthClient {

    boolean supports(VerificationProvider verificationProvider);
    String getAuthorizationUrl();
    OauthUser fetchUser(String authorizationCode);
}