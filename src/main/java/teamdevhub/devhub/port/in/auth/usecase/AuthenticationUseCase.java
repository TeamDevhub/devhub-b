package teamdevhub.devhub.port.in.auth.usecase;

import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;

public interface AuthenticationUseCase {

    LoginResponseDto login(AuthenticatedUser authenticatedUser);
    TokenResponseDto reissueAccessToken(AuthenticatedUser authenticatedUser);
    void revoke(String userGuid);
}
