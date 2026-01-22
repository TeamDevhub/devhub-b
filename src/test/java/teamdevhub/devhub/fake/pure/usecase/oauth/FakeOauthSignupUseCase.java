package teamdevhub.devhub.fake.pure.usecase.oauth;

import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthSignupUseCase;

import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_OAUTH_ID_1;

public class FakeOauthSignupUseCase implements OauthSignupUseCase {

    @Override
    public OauthUser signupWithOauth(SignupOauthUserCommand signupOauthUserCommand) {
        return new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
    }
}
