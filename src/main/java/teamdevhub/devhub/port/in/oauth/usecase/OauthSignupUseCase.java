package teamdevhub.devhub.port.in.oauth.usecase;

import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;

public interface OauthSignupUseCase {

    OauthUser signupWithOauth(SignupOauthUserCommand signupOauthUserCommand);
}
