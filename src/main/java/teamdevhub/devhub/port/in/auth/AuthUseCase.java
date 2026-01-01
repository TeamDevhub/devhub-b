package teamdevhub.devhub.port.in.auth;

import teamdevhub.devhub.adapter.in.auth.dto.TokenResponseDto;

public interface AuthUseCase {
    TokenResponseDto refreshAccessToken(String refreshToken);
    void logout(String email);
}
