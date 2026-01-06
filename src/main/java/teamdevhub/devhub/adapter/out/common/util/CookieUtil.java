package teamdevhub.devhub.adapter.out.common.util;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    private static final String cookieName = "refreshToken";
    private static final String sameSite = "SameSite";
    private static final String path = "/auth/reissue";
    private static final int maxAgeSeconds = 14 * 24 * 60 * 60;

    private CookieUtil() {}

    public static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(cookieName, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite(sameSite)
                .path(path)
                .maxAge(maxAgeSeconds)
                .build();
    }
}