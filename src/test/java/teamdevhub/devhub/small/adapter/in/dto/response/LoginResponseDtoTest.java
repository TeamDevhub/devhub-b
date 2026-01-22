package teamdevhub.devhub.small.adapter.in.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginResponseDtoTest {

    @Test
    @DisplayName("LoginResponseDto_를_생성하고_AuthorizationHeader_를_확인할_수_있다")
    void canGetAuthorizationHeaderAfterCreatingDto() {
        // given
        String accessToken = "access-token-random";
        String refreshToken = "refresh-token-random";

        // when
        LoginResponseDto loginResponseDto = LoginResponseDto.of(accessToken, refreshToken);

        // then
        assertThat(loginResponseDto.toAuthorizationHeader()).isEqualTo("Bearer access-token-random");
        assertThat(loginResponseDto.getAccessToken()).isEqualTo(accessToken);
        assertThat(loginResponseDto.getRefreshToken()).isEqualTo(refreshToken);
    }
}
