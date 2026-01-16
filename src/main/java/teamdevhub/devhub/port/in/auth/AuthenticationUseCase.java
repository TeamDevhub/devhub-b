package teamdevhub.devhub.port.in.auth;

import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.dto.response.auth.LoginResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.auth.TokenResponseDto;

public interface AuthenticationUseCase {
    LoginResponseDto login(LoginCommand loginCommand);
    void issueRefreshToken(String email, String refreshToken);
    TokenResponseDto reissueAccessToken(String refreshToken);
    void revoke(String userGuid);
}
