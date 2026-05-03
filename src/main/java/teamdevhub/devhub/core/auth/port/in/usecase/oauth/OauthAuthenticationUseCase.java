package teamdevhub.devhub.core.auth.port.in.usecase.oauth;

import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthorizationResult;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;

public interface OauthAuthenticationUseCase {

    OauthAuthorizationResult createAuthorizationUrl(String provider);
    OauthUser handleOAuthCallback(VerificationProvider verificationProvider, String code);
    String issueTempToken(OauthUser oauthUser);
}
