package teamdevhub.devhub.api.auth.controller;

import org.springframework.http.ResponseCookie;

public class CookieFactory {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final String SAME_SITE = "Lax";
    private static final String PATH = "/auth/reissue";
    private static final int MAX_AGE_SECONDS = 14 * 24 * 60 * 60;

    private CookieFactory() {}

    public static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite(SAME_SITE)
                .path(PATH)
                .maxAge(MAX_AGE_SECONDS)
                .build();
    }
}