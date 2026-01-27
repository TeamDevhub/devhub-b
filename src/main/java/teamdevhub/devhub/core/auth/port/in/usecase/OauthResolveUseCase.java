package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.core.auth.application.service.vo.OauthUserResult;
import teamdevhub.devhub.core.auth.domain.vo.user.OauthUser;
import teamdevhub.devhub.core.auth.port.in.command.SignupOauthUserCommand;

public interface OauthResolveUseCase {

    OauthUserResult findOrRequireSignup(OauthUser oauthUser);
    OauthUser extractOauthUser(SignupOauthUserCommand signupOauthUserCommand);
}
