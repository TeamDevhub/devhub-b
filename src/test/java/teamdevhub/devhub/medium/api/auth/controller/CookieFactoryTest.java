package teamdevhub.devhub.medium.api.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import teamdevhub.devhub.api.auth.controller.CookieFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieFactoryTest {

    private static final String TEST_REFRESH_TOKEN = "sample-refresh-token";

    @Test
    @DisplayName("refreshToken_쿠키를_생성하면_속성과_값이_올바르게_설정된다")
    void createRefreshTokenCookieSetsCorrectValues() {
        // when
        ResponseCookie cookie = CookieFactory.createRefreshTokenCookie(TEST_REFRESH_TOKEN);

        // then
        assertThat(cookie.getName()).isEqualTo("refreshToken");
        assertThat(cookie.getValue()).isEqualTo(TEST_REFRESH_TOKEN);
        assertThat(cookie.getPath()).isEqualTo("/auth/reissue");
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.isSecure()).isFalse();
        assertThat(cookie.getSameSite()).isEqualTo("Lax");
        assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(14 * 24 * 60 * 60);
    }
}
