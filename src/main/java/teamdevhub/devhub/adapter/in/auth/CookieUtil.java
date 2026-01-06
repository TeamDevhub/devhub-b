package teamdevhub.devhub.adapter.in.auth;

import org.springframework.http.ResponseCookie;

class CookieUtil {

    private static final String COOKIE_NAME = "refreshToken";
    private static final String SAME_SITE = "SameSite";
    private static final String PATH = "/auth/reissue";
    private static final int MAX_AGE_SECONDS = 14 * 24 * 60 * 60;

    private CookieUtil() {}

    static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE)
                .path(PATH)
                .maxAge(MAX_AGE_SECONDS)
                .build();
    }
}