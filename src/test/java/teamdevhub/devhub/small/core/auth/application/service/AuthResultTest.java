package teamdevhub.devhub.small.core.auth.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.AuthResult;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthResultTest {

    @Test
    @DisplayName("LoginResponseDto_를_생성하고_AuthorizationHeader_를_확인할_수_있다")
    void canGetAuthorizationHeaderAfterCreatingDto() {
        // given
        String accessToken = "access-token-random";
        String refreshToken = "refresh-token-random";

        // when
        AuthResult authResult = AuthResult.of(accessToken, refreshToken);

        // then
        assertThat(authResult.toauthorizationHeader()).isEqualTo("Bearer access-token-random");
        assertThat(authResult.accessToken()).isEqualTo(accessToken);
        assertThat(authResult.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("of_로_생성한_AuthResult_는_hasRefreshToken_이_true_다")
    void hasRefreshToken_whenCreatedWithRefreshToken_returnsTrue() {
        // given
        AuthResult authResult = AuthResult.of("access-token", "refresh-token");

        // when, then
        assertThat(authResult.hasRefreshToken()).isTrue();
    }

    @Test
    @DisplayName("refreshToken_이_없으면_hasRefreshToken_이_false_다")
    void hasRefreshToken_whenNoRefreshToken_returnsFalse() {
        // given
        AuthResult authResult = AuthResult.of("access-token", null);

        // when, then
        assertThat(authResult.hasRefreshToken()).isFalse();
    }
}
