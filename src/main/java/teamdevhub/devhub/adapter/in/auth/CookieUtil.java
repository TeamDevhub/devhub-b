package teamdevhub.devhub.adapter.in.auth;

import org.springframework.http.ResponseCookie;

class CookieUtil {

    private static final String cookieName = "refreshToken";
    private static final String sameSite = "SameSite";
    private static final String path = "/auth/reissue";
    private static final int maxAgeSeconds = 14 * 24 * 60 * 60;

    private CookieUtil() {}

    static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(cookieName, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite(sameSite)
                .path(path)
                .maxAge(maxAgeSeconds)
                .build();
    }
}