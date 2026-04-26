package teamdevhub.devhub.small.core.auth.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeUserCredentialUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeLoginPolicyUseCase;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.facade.AuthFacade;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AuthFacadeTest {

    private AuthFacade authFacade;

    private FakeUserCredentialUseCase userCredentialUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeLoginPolicyUseCase userLoginUseCase;

    @BeforeEach
    void init() {
        userCredentialUseCase = new FakeUserCredentialUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        userLoginUseCase = new FakeLoginPolicyUseCase();

        authFacade = new AuthFacade(
                userCredentialUseCase,
                authenticationUseCase,
                userLoginUseCase
        );
    }

    @Test
    @DisplayName("loginCommand_로_로그인_할_수_있다")
    void loginWithLoginCommand() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        AuthResult authResult = authFacade.login(loginCommand);

        // then
        assertThat(authResult.accessToken()).isEqualTo("access-token");
    }

    @Test
    @DisplayName("reissueAccessToken_는_새로운_accessToken_을_반환한다")
    void reissueAccessToken_ReturnsNewAccessToken() {
        // given
        String oldToken = "old-token";

        // when
        AuthResult authResult = authFacade.reissueAccessToken(oldToken);

        // then
        assertThat(authResult.accessToken()).isEqualTo("new-access-token");
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

