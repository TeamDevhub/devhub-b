package teamdevhub.devhub.small.adapter.in.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.AuthFacade;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthCallbackUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthLoginUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthSignupUseCase;
import teamdevhub.devhub.fake.pure.usecase.verification.FakeVerificationUseCase;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AuthFacadeTest {

    private FakeAuthenticationUseCase fakeAuthenticationUseCase;
    private FakeOauthLoginUseCase fakeOauthLoginUseCase;


    private AuthFacade authFacade;


    @BeforeEach
    void init() {
        fakeAuthenticationUseCase = new FakeAuthenticationUseCase();
        FakeVerificationUseCase fakeVerificationUseCase = new FakeVerificationUseCase();
        FakeOauthCallbackUseCase fakeOauthCallbackUseCase = new FakeOauthCallbackUseCase();
        fakeOauthLoginUseCase = new FakeOauthLoginUseCase();
        FakeOauthSignupUseCase fakeOauthSignupUseCase = new FakeOauthSignupUseCase();

        authFacade = new AuthFacade(
                fakeAuthenticationUseCase,
                fakeVerificationUseCase,
                fakeOauthCallbackUseCase,
                fakeOauthLoginUseCase,
                fakeOauthSignupUseCase
        );
    }

    @Test
    @DisplayName("login은_authenticationUseCase로_위임된다")
    void loginDelegatesToAuthenticationUseCase() {
        // given
        LoginCommand command = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        LoginResponseDto response = authFacade.login(command);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
    }

    @Test
    @DisplayName("signupWithOauth_는_signup_후_login_을_연속_호출한다")
    void signupWithOauth_callsSignupThenLogin() {
        // given
        OauthSignupCommand signupCommand = new OauthSignupCommand("TEMP_TOKEN", TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        LoginResponseDto response = authFacade.signupWithOauth(signupCommand);

        // then
        assertThat(response.getAccessToken()).isEqualTo("oauth-access");
        assertThat(fakeOauthLoginUseCase.getLastCommand().tempToken()).isEqualTo("TEMP_TOKEN");
    }

    @Test
    @DisplayName("createOAuthAuthorizationUrl_은_callbackUseCase_에_위임된다")
    void createOAuthAuthorizationUrlDelegates() {
        // when
        String url = authFacade.createOAuthAuthorizationUrl("github");

        // then
        assertThat(url).isEqualTo("https://oauth.test/github");
    }

    @Test
    @DisplayName("logout_은_revoke_를_호출한다")
    void logoutRevokesToken() {
        // when
        authFacade.logout(TEST_USER_GUID_1);

        // then
        assertThat(fakeAuthenticationUseCase.getRevokedUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }
}

