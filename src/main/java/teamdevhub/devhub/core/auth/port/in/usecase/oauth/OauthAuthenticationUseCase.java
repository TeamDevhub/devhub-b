package teamdevhub.devhub.core.auth.port.in.usecase.oauth;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.OauthUser;

public interface OauthAuthenticationUseCase {

    String createAuthorizationUrl(String provider);
    OauthUser handleOAuthCallback(VerificationProvider verificationProvider, String code);
    String issueTempToken(OauthUser oauthUser);
}
