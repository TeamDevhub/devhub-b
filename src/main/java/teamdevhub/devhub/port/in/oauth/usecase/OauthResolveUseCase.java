package teamdevhub.devhub.port.in.oauth.usecase;

import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;

public interface OauthResolveUseCase {

    OauthUserResult resolveOauthUser(ResolveOauthUserCommand resolveOauthUserCommand);
}
