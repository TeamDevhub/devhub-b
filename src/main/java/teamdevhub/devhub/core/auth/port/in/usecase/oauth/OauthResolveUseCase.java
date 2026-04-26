package teamdevhub.devhub.core.auth.port.in.usecase.oauth;

import teamdevhub.devhub.core.auth.application.service.oauth.OauthUserResult;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;

public interface OauthResolveUseCase {

    OauthUserResult findOrRequireSignup(OauthUser oauthUser);
    OauthUser extractOauthUser(SignupOauthUserCommand signupOauthUserCommand);
}
