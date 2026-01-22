package teamdevhub.devhub.small.port.in.user.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthResolveUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthSignupUseCase;
import teamdevhub.devhub.fake.pure.usecase.user.FakeUserSignupUseCase;
import teamdevhub.devhub.fake.pure.usecase.verification.FakeVerificationUseCase;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.user.UserSignupFacade;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupFacadeTest {

    private UserSignupFacade userSignupFacade;

    private FakeUserSignupUseCase userSignupUseCase;
    private FakeOauthResolveUseCase oauthResolveUseCase;
    private FakeOauthSignupUseCase oauthSignupUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeVerificationUseCase verificationUseCase;

    @BeforeEach
    void init() {
        userSignupUseCase = new FakeUserSignupUseCase();
        oauthResolveUseCase = new FakeOauthResolveUseCase();
        oauthSignupUseCase = new FakeOauthSignupUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        verificationUseCase = new FakeVerificationUseCase();

        userSignupFacade = new UserSignupFacade(
                userSignupUseCase,
                oauthResolveUseCase,
                oauthSignupUseCase,
                authenticationUseCase,
                verificationUseCase
        );
    }

    @Test
    @DisplayName("signup_는_사용자_정보를_정상적으로_반환한다")
    void signupReturnsUser() {
        // given
        SignupUserCommand signupUserCommand = new SignupUserCommand(
                null,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST,
                VERIFICATION_TARGET_1
        );

        // when
        User user = userSignupFacade.signup(signupUserCommand);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO_1);
    }

    /**
     * 테스트케이스 보완 필요
    @Test
    @DisplayName("signupWithOauth_는_signup_후_login_을_연속_호출한다")
    void signupWithOauth_callsSignupThenLogin() {
        // given
        SignupOauthUserCommand signupCommand = new SignupOauthUserCommand(TEMP_TOKEN, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        OauthAuthResponseDto oauthAuthResponseDto = userSignupFacade.signupWithOauth(signupCommand);

        // then
        assertThat(oauthAuthResponseDto.getAccessToken()).isEqualTo("oauth-access");
        assertThat(oauthResolveUseCase.getLastCommand().tempToken()).isEqualTo("TEMP_TOKEN");
    }
     */
}
