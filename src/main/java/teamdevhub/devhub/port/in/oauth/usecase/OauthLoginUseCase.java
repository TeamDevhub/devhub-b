package teamdevhub.devhub.port.in.oauth.usecase;

import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.port.in.oauth.command.OauthLoginCommand;

public interface OauthLoginUseCase {

    LoginResponseDto loginWithOauth(OauthLoginCommand oauthLoginCommand);
}
