package teamdevhub.devhub.fake.pure.usecase.oauth;

import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.port.in.oauth.command.OauthLoginCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthLoginUseCase;

public class FakeOauthLoginUseCase implements OauthLoginUseCase {

    private OauthLoginCommand lastCommand;

    @Override
    public LoginResponseDto loginWithOauth(OauthLoginCommand command) {
        this.lastCommand = command;
        return LoginResponseDto.existedOAuthUser(
                "Bearer",
                "oauth-access",
                "oauth-refresh",
                SignupStatus.COMPLETED
        );
    }

    public OauthLoginCommand getLastCommand() {
        return lastCommand;
    }
}