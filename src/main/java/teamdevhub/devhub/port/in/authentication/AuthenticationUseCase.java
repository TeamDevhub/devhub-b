package teamdevhub.devhub.port.in.authentication;

import teamdevhub.devhub.port.in.authentication.command.LoginCommand;
import teamdevhub.devhub.adapter.in.dto.response.auth.LoginResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.auth.TokenResponseDto;

public interface AuthenticationUseCase {
    LoginResponseDto login(LoginCommand loginCommand);
    void issueRefreshToken(String email, String refreshToken);
    TokenResponseDto reissueAccessToken(String refreshToken);
    void revoke(String userGuid);
}
