package teamdevhub.devhub.api.auth.controller;

import org.springframework.http.ResponseCookie;

public class CookieFactory {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final String TEMP_COOKIE_NAME = "tempToken";
    private static final String SAME_SITE = "SameSite";
    private static final String PATH = "/auth/reissue";
    private static final int MAX_AGE_SECONDS = 14 * 24 * 60 * 60;

    private CookieFactory() {}

    public static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE)
                .path(PATH)
                .maxAge(MAX_AGE_SECONDS)
                .build();
    }

    public static ResponseCookie createTempTokenCookie(String tempToken) {
        return ResponseCookie.from(TEMP_COOKIE_NAME, tempToken)
                .httpOnly(true)
                //.secure(true)
                .sameSite("None")
                //.path("/")
                .maxAge(60 * 10)
                .build();
    }
}