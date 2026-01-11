package teamdevhub.devhub.small.adapter.in.auth.dto.response;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginResponseDtoTest {

    @Test
    void LoginResponseDto_를_생성하고_toAuthorizationHeader_를_확인할_수_있다() {
        // given
        String prefix = "Bearer";
        String accessToken = "access-token-random";
        String refreshToken = "refresh-token-random";

        // when
        LoginResponseDto loginResponseDto = LoginResponseDto.of(prefix, accessToken, refreshToken);

        // then
        assertThat(loginResponseDto).isNotNull();
        assertThat(prefix).isEqualTo(loginResponseDto.getPrefix());
        assertThat(accessToken).isEqualTo(loginResponseDto.getAccessToken());
        assertThat(refreshToken).isEqualTo(loginResponseDto.getRefreshToken());
        assertThat("Bearer access-token-random").isEqualTo(loginResponseDto.toAuthorizationHeader());
    }
}
