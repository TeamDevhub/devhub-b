package teamdevhub.devhub.fake.pure.usecase.oauth;

import teamdevhub.devhub.application.service.oauth.vo.OauthCallbackResult;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.in.oauth.usecase.OauthAuthenticationUseCase;

public class FakeOauthAuthenticationUseCase implements OauthAuthenticationUseCase {

    @Override
    public String createAuthorizationUrl(String provider) {
        return "https://oauth.test/" + provider;
    }

    @Override
    public OauthCallbackResult handleOAuthCallback(VerificationProvider verificationProvider, String code) {
        return OauthCallbackResult.existedUser("TEMP_TOKEN");
    }
}
