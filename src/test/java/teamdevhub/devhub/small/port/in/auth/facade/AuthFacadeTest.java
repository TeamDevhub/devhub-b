package teamdevhub.devhub.small.port.in.auth.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.fake.pure.provider.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticatedUserUseCase;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthResolveUseCase;
import teamdevhub.devhub.fake.pure.usecase.user.FakeUserLoginUseCase;
import teamdevhub.devhub.port.in.auth.AuthFacade;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AuthFacadeTest {

    private AuthFacade authFacade;

    private FakeAuthenticatedUserUseCase authenticatedUserUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeOauthResolveUseCase oauthResolveUseCase;
    private FakeUserLoginUseCase userLoginUseCase;
    private FakeAuthenticatedUserResolver authenticatedUserResolver;

    @BeforeEach
    void init() {
        authenticatedUserUseCase = new FakeAuthenticatedUserUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        oauthResolveUseCase = new FakeOauthResolveUseCase();
        userLoginUseCase = new FakeUserLoginUseCase();
        authenticatedUserResolver = new FakeAuthenticatedUserResolver();

        authFacade = new AuthFacade(
                authenticatedUserUseCase,
                authenticationUseCase,
                oauthResolveUseCase,
                userLoginUseCase,
                authenticatedUserResolver
        );
    }

    @Test
    @DisplayName("loginCommand_로_로그인_할_수_있다.")
    void loginWithLoginCommand() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        LoginResponseDto loginResponseDto = authFacade.login(loginCommand);

        // then
        assertThat(loginResponseDto.getAccessToken()).isEqualTo("access-token");
    }

    @Test
    @DisplayName("loginWithOauth_로그인_가능한_경우_accessToken_을_반환한다.")
    void loginWithOauth_WhenLoginAvailable_ReturnsLoginResponse() {
        // given
        oauthResolveUseCase.setLoginAvailableScenario(true);
        ResolveOauthUserCommand resolveOauthUserCommand = new ResolveOauthUserCommand(TEMP_TOKEN);

        // when
        OauthAuthResponseDto oauthAuthResponseDto = authFacade.loginWithOauth(resolveOauthUserCommand);

        // then
        assertThat(oauthAuthResponseDto.getAccessToken()).isEqualTo("access-token");
        assertThat(oauthResolveUseCase.getLastCommand()).isEqualTo(resolveOauthUserCommand);
    }

    @Test
    @DisplayName("loginWithOauth_회원가입_필요한_경우_tempToken_을_반환한다.")
    void loginWithOauth_WhenSignupRequired_ReturnsTempToken() {
        // given
        oauthResolveUseCase.setLoginAvailableScenario(false);
        ResolveOauthUserCommand resolveOauthUserCommand = new ResolveOauthUserCommand(TEMP_TOKEN);

        // when
        OauthAuthResponseDto oauthAuthResponseDto = authFacade.loginWithOauth(resolveOauthUserCommand);

        // then
        assertThat(oauthAuthResponseDto.getTempToken()).isEqualTo(TEMP_TOKEN);
        assertThat(oauthResolveUseCase.getLastCommand()).isEqualTo(resolveOauthUserCommand);
    }

    @Test
    @DisplayName("reissueAccessToken_는_새로운_accessToken_을_반환한다")
    void reissueAccessToken_ReturnsNewAccessToken() {
        // given
        String oldToken = "old-token";

        // when
        TokenResponseDto tokenResponseDto = authFacade.reissueAccessToken(oldToken);

        // then
        assertThat(tokenResponseDto.getAccessToken()).isEqualTo("new-access-token");
    }

    @Test
    @DisplayName("logout_은_revoke_를_호출한다")
    void logoutRevokesToken() {
        // given, when
        authFacade.logout(TEST_USER_GUID_1);

        // then
        assertThat(authenticationUseCase.getRevokedUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }
}

