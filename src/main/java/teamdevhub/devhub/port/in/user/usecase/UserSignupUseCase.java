package teamdevhub.devhub.port.in.user.usecase;

import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;
import teamdevhub.devhub.port.in.user.command.AdminSignupCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

public interface UserSignupUseCase {

    void initializeAdminUser(AdminSignupCommand adminSignupCommand);
    User signup(SignupCommand signupCommand);
    void signupWithOauth(OauthSignupCommand oauthSignupCommand, OauthUser oauthUser);
}
