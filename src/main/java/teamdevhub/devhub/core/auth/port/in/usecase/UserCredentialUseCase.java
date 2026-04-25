package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;

public interface UserCredentialUseCase {

    String signupEmailUser(SignupUserCommand signupUserCommand);
    UserCredential signupOAuthUser(OauthUser oauthUser);
    UserCredential getUserForReissue(String refreshToken);
    UserCredential authenticate(LoginCommand loginCommand);
}
