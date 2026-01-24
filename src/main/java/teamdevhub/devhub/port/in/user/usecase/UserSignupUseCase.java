package teamdevhub.devhub.port.in.user.usecase;

import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.user.command.SignupAdminCommand;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;

public interface UserSignupUseCase {

    void initializeAdminUser(SignupAdminCommand signupAdminCommand);
    void signup(SignupUserCommand signupUserCommand);
    User signupWithOauth(SignupOauthUserCommand signupOauthUserCommand, OauthUser oauthUser);
}
