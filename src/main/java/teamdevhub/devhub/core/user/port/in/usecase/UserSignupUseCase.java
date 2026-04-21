package teamdevhub.devhub.core.user.port.in.usecase;

import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

public interface UserSignupUseCase {

    void initializeAdminUser(SignupAdminCommand signupAdminCommand);
    void saveEmailUserInfo(SignupUserCommand signupUserCommand, String userGuid);
    void saveOAuthUserInfo(SignupOauthUserCommand signupOauthUserCommand, OauthUser oauthUser, String userGuid);
}
