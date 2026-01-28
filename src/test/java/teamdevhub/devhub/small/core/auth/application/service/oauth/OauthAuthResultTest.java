package teamdevhub.devhub.small.core.auth.application.service.oauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthResult;
import teamdevhub.devhub.shared.enums.SignupStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class OauthAuthResultTest {

    @Test
    @DisplayName("loggedIn_팩토리_메서드로_로그인_완료_OauthAuthResponseDto_를_생성할_수_있다")
    void canCreateLoggedInDto() {
        // given
        AuthResult authResult = AuthResult.of(ACCESS_TOKEN, REFRESH_TOKEN);

        // when
        OauthAuthResult oauthAuthResult = OauthAuthResult.loggedIn(authResult);

        // then
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(oauthAuthResult.accessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(oauthAuthResult.refreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(oauthAuthResult.tempToken()).isNull();
    }

    @Test
    @DisplayName("requiresSignup_팩토리_메서드로_회원가입_필요_OauthAuthResponseDto_를_생성할_수_있다")
    void canCreateRequiresSignupDto() {
        // when
        OauthAuthResult oauthAuthResult = OauthAuthResult.requiresSignup(TEMP_TOKEN);

        // then
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.PENDING);
        assertThat(oauthAuthResult.tempToken()).isEqualTo(TEMP_TOKEN);
        assertThat(oauthAuthResult.accessToken()).isNull();
        assertThat(oauthAuthResult.refreshToken()).isNull();
    }

    @Test
    @DisplayName("toAuthorizationHeader_로_Bearer_토큰_헤더를_생성할_수_있다")
    void canGetAuthorizationHeader() {
        // given
        OauthAuthResult oauthAuthResult = OauthAuthResult.builder()
                .accessToken(ACCESS_TOKEN)
                .build();

        // when
        String header = oauthAuthResult.toAuthorizationHeader();

        // then
        assertThat(header).isEqualTo("Bearer access-token-1");
    }
}
