package teamdevhub.devhub.port.in.oauth.usecase;

import teamdevhub.devhub.application.service.oauth.vo.OauthCallbackResult;
import teamdevhub.devhub.common.enums.VerificationProvider;

public interface OauthAuthenticationUseCase {

    String createAuthorizationUrl(String provider);
    OauthCallbackResult handleOAuthCallback(VerificationProvider verificationProvider, String code);
}
