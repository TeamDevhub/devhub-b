package teamdevhub.devhub.api.auth.controller;

import org.springframework.http.ResponseCookie;

public class CookieFactory {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final String OAUTH_STATE_COOKIE_NAME = "oauthState";
    private static final String SAME_SITE = "Lax";
    private static final String PATH = "/auth/reissue";
    private static final String OAUTH_PATH = "/auth/oauth";
    private static final int MAX_AGE_SECONDS = 14 * 24 * 60 * 60;
    private static final int OAUTH_STATE_MAX_AGE_SECONDS = 300;

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

    public static ResponseCookie createOauthStateCookie(String state) {
        return ResponseCookie.from(OAUTH_STATE_COOKIE_NAME, state)
                .httpOnly(true)
                .secure(false)
                .sameSite(SAME_SITE)
                .path(OAUTH_PATH)
                .maxAge(OAUTH_STATE_MAX_AGE_SECONDS)
                .build();
    }

    public static ResponseCookie expireOauthStateCookie() {
        return ResponseCookie.from(OAUTH_STATE_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false)
                .sameSite(SAME_SITE)
                .path(OAUTH_PATH)
                .maxAge(0)
                .build();
    }
}