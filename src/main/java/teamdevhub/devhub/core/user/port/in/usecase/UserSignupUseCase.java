package teamdevhub.devhub.core.user.port.in.usecase;

import teamdevhub.devhub.core.auth.domain.vo.user.OauthUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.auth.port.in.command.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

public interface UserSignupUseCase {

    void initializeAdminUser(SignupAdminCommand signupAdminCommand);
    void signup(SignupUserCommand signupUserCommand);
    User signupWithOauth(SignupOauthUserCommand signupOauthUserCommand, OauthUser oauthUser);
}
