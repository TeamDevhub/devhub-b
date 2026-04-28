package teamdevhub.devhub.core.auth.application.service;

import lombok.Builder;
import teamdevhub.devhub.outbound.auth.infrastructure.token.TokenPrefix;

@Builder
public record AuthResult(String accessToken, String refreshToken) {

    public static AuthResult of(String accessToken, String refreshToken) {
        return new AuthResult(accessToken, refreshToken);
    }

    public boolean hasRefreshToken() {
        return refreshToken != null;
    }

    public String toAuthorizationHeader() {
        return TokenPrefix.BEARER.withToken(accessToken);
    }
}
