package teamdevhub.devhub.port.in.oauth.usecase;

import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;

public interface OauthAuthenticationUseCase {

    String createAuthorizationUrl(String provider);
    OauthUser handleOAuthCallback(VerificationProvider verificationProvider, String code);
    String issueTempToken(OauthUser oauthUser);
}
