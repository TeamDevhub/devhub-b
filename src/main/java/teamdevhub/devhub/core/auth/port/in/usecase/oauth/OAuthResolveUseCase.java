package teamdevhub.devhub.core.auth.port.in.usecase.oauth;

import teamdevhub.devhub.core.auth.application.service.oauth.vo.OAuthUserResult;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOAuthUserCommand;

public interface OAuthResolveUseCase {

    OAuthUserResult findOrRequireSignup(OAuthUser oAuthUser);
    OAuthUser extractOAuthUser(SignupOAuthUserCommand signupOAuthUserCommand);
}
