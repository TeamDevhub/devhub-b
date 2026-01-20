package teamdevhub.devhub.fake.pure.usecase.oauth;

import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthSignupUseCase;

public class FakeOauthSignupUseCase implements OauthSignupUseCase {

    @Override
    public String signupWithOauth(OauthSignupCommand oauthSignupCommand) {
        return "TEMP_TOKEN";
    }
}
