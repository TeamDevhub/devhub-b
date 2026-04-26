package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdatePasswordCommand;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;

public interface UserCredentialUseCase {

    String signupEmailUser(SignupUserCommand signupUserCommand);
    AuthenticatedUser signupOAuthUser(OauthUser oauthUser);
    AuthenticatedUser getUserForReissue(String refreshToken);
    AuthenticatedUser authenticate(LoginCommand loginCommand);
    void updatePassword(UpdatePasswordCommand updatePasswordCommand);
}
