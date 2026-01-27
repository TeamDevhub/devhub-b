package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.user.OauthUser;

public interface OauthClient {

    boolean supports(VerificationProvider verificationProvider);
    String getAuthorizationUrl();
    OauthUser fetchUser(String authorizationCode);
}