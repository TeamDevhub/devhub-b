package teamdevhub.devhub.small.port.in.user.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.oauth.vo.OauthAuthResult;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthResolveUseCase;
import teamdevhub.devhub.fake.pure.usecase.user.FakeUserSignupUseCase;
import teamdevhub.devhub.fake.pure.usecase.verification.FakeVerificationUseCase;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;
import teamdevhub.devhub.port.in.user.facade.UserSignupFacade;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupFacadeTest {

    private UserSignupFacade userSignupFacade;

    private FakeUserSignupUseCase userSignupUseCase;
    private FakeOauthResolveUseCase oauthResolveUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeVerificationUseCase verificationUseCase;

    @BeforeEach
    void init() {
        userSignupUseCase = new FakeUserSignupUseCase();
        oauthResolveUseCase = new FakeOauthResolveUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        verificationUseCase = new FakeVerificationUseCase();

        userSignupFacade = new UserSignupFacade(
                userSignupUseCase,
                oauthResolveUseCase,
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
        userSignupFacade.signup(signupUserCommand);

        // then
        assertThat(userSignupUseCase.isSignupCalled()).isTrue();
    }

    @Test
    @DisplayName("signupWithOauth_는_signup_후_oauth-access-token_을_포함한_로그인_성공으로_이어진다")
    void signupWithOauthCallsSignupThenLogin() {
        // given
        SignupOauthUserCommand signupCommand = new SignupOauthUserCommand(TEMP_TOKEN, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        OauthAuthResult oauthAuthResult = userSignupFacade.signupWithOauth(signupCommand);

        // then
        assertThat(oauthAuthResult.accessToken()).isEqualTo("access-token");
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(oauthAuthResult.tempToken()).isNull();
    }
}
