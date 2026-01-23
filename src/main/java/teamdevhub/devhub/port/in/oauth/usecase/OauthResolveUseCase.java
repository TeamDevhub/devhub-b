package teamdevhub.devhub.port.in.oauth.usecase;

import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;

public interface OauthResolveUseCase {

    OauthUserResult findOrRequireSignup(ResolveOauthUserCommand resolveOauthUserCommand);
    OauthUser extractOauthUser(SignupOauthUserCommand signupOauthUserCommand);
}
