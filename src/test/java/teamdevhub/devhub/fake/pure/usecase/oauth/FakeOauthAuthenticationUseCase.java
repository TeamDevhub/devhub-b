package teamdevhub.devhub.fake.pure.usecase.oauth;

import lombok.Setter;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.usecase.OauthAuthenticationUseCase;

import java.util.Objects;

import static teamdevhub.devhub.constant.UserTestConstant.*;

@Setter
public class FakeOauthAuthenticationUseCase implements OauthAuthenticationUseCase {

    private OauthUser oauthUser;
    private String tempToken;

    @Override
    public String createAuthorizationUrl(String provider) {
        return "https://oauth.test/" + provider;
    }

    @Override
    public OauthUser handleOAuthCallback(VerificationProvider verificationProvider, String code) {
        return Objects.requireNonNullElseGet(oauthUser, () -> new OauthUser(TEST_OAUTH_ID_1, verificationProvider, TEST_EMAIL_1));
    }

    @Override
    public String issueTempToken(OauthUser oauthUser) {
        return Objects.requireNonNullElse(tempToken, TEMP_TOKEN);
    }
}
