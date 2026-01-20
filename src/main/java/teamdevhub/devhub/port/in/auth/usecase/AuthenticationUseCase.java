package teamdevhub.devhub.port.in.auth.usecase;

import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

public interface AuthenticationUseCase {

    LoginResponseDto login(LoginCommand loginCommand);
    TokenResponseDto reissueAccessToken(String refreshToken);
    void revoke(String userGuid);
}
