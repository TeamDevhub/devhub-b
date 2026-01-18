package teamdevhub.devhub.port.in.auth;

import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.dto.response.auth.LoginResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.auth.TokenResponseDto;

public interface AuthSessionUseCase {

    LoginResponseDto login(LoginCommand loginCommand);
    TokenResponseDto reissueAccessToken(String refreshToken);
    void revoke(String userGuid);
}
