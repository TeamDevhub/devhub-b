package teamdevhub.devhub.port.in.auth;

import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;

public interface AuthUseCase {
    LoginResponseDto login(LoginCommand loginCommand);
    void issueRefreshToken(String email, String refreshToken);
    TokenResponseDto reissueAccessToken(String refreshToken);
    void revoke(String email);
}
