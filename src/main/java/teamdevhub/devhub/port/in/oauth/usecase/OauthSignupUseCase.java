package teamdevhub.devhub.port.in.oauth.usecase;

import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;

public interface OauthSignupUseCase {

    String signupWithOauth(OauthSignupCommand oauthSignupCommand);
}
