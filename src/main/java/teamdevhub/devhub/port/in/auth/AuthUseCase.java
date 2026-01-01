package teamdevhub.devhub.port.in.auth;

import teamdevhub.devhub.adapter.in.auth.dto.TokenResponseDto;

public interface AuthUseCase {
    TokenResponseDto reissueAccessToken(String refreshToken);
    void revoke(String email);
}
