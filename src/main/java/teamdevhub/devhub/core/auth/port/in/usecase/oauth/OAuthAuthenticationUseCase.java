package teamdevhub.devhub.core.auth.port.in.usecase.oauth;

import teamdevhub.devhub.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OAuthUser;

public interface OAuthAuthenticationUseCase {

    OAuthAuthorizationResult createAuthorizationUrl(String provider);
    OAuthUser handleOAuthCallback(VerificationProvider verificationProvider, String code);
    String issueTempToken(OAuthUser oauthUser);
}
