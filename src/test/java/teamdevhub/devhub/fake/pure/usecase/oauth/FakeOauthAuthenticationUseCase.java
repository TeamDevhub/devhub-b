package teamdevhub.devhub.fake.pure.usecase.oauth;

import lombok.Setter;
import teamdevhub.devhub.application.service.oauth.vo.OauthCallbackResult;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.in.oauth.usecase.OauthAuthenticationUseCase;

import java.util.Objects;

import static teamdevhub.devhub.constant.UserTestConstant.TEMP_TOKEN;

@Setter
public class FakeOauthAuthenticationUseCase implements OauthAuthenticationUseCase {

    private OauthCallbackResult callbackResult;

    @Override
    public String createAuthorizationUrl(String provider) {
        return "https://oauth.test/" + provider;
    }

    @Override
    public OauthCallbackResult handleOAuthCallback(VerificationProvider verificationProvider, String code) {
        return Objects.requireNonNullElseGet(callbackResult, () -> OauthCallbackResult.existedUser(TEMP_TOKEN));
    }
}
