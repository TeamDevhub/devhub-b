package teamdevhub.devhub.small.adapter.in.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.application.service.oauth.vo.OauthCallbackResult;
import teamdevhub.devhub.common.enums.SignupStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class OauthAuthResponseDtoTest {

    @Test
    @DisplayName("fromCallback_팩토리_메서드로_OauthAuthResponseDto_를_생성할_수_있다")
    void canCreateDtoFromCallback() {
        // given
        OauthCallbackResult callbackResult = new OauthCallbackResult(SignupStatus.PENDING, TEMP_TOKEN);

        // when
        OauthAuthResponseDto oauthAuthResponseDto = OauthAuthResponseDto.fromCallback(callbackResult);

        // then
        assertThat(oauthAuthResponseDto.getSignupStatus()).isEqualTo(SignupStatus.PENDING);
        assertThat(oauthAuthResponseDto.getTempToken()).isEqualTo(TEMP_TOKEN);
        assertThat(oauthAuthResponseDto.getAccessToken()).isNull();
        assertThat(oauthAuthResponseDto.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("loggedIn_팩토리_메서드로_로그인_완료_OauthAuthResponseDto_를_생성할_수_있다")
    void canCreateLoggedInDto() {
        // given
        LoginResponseDto loginResponseDto = LoginResponseDto.of(ACCESS_TOKEN, REFRESH_TOKEN);

        // when
        OauthAuthResponseDto oauthAuthResponseDto = OauthAuthResponseDto.loggedIn(loginResponseDto);

        // then
        assertThat(oauthAuthResponseDto.getSignupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(oauthAuthResponseDto.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(oauthAuthResponseDto.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(oauthAuthResponseDto.getTempToken()).isNull();
    }

    @Test
    @DisplayName("requiresSignup_팩토리_메서드로_회원가입_필요_OauthAuthResponseDto_를_생성할_수_있다")
    void canCreateRequiresSignupDto() {
        // when
        OauthAuthResponseDto oauthAuthResponseDto = OauthAuthResponseDto.requiresSignup(TEMP_TOKEN);

        // then
        assertThat(oauthAuthResponseDto.getSignupStatus()).isEqualTo(SignupStatus.PENDING);
        assertThat(oauthAuthResponseDto.getTempToken()).isEqualTo(TEMP_TOKEN);
        assertThat(oauthAuthResponseDto.getAccessToken()).isNull();
        assertThat(oauthAuthResponseDto.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("toAuthorizationHeader_로_Bearer_토큰_헤더를_생성할_수_있다")
    void canGetAuthorizationHeader() {
        // given
        OauthAuthResponseDto oauthAuthResponseDto = OauthAuthResponseDto.builder()
                .accessToken(ACCESS_TOKEN)
                .build();

        // when
        String header = oauthAuthResponseDto.toAuthorizationHeader();

        // then
        assertThat(header).isEqualTo("Bearer access-token-1");
    }
}
