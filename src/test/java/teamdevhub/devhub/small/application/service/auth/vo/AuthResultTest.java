package teamdevhub.devhub.small.application.service.auth.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.vo.AuthResult;

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
        assertThat(authResult.toAuthorizationHeader()).isEqualTo("Bearer access-token-random");
        assertThat(authResult.accessToken()).isEqualTo(accessToken);
        assertThat(authResult.refreshToken()).isEqualTo(refreshToken);
    }
}
