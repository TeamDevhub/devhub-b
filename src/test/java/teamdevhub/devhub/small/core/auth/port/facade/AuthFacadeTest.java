package teamdevhub.devhub.small.core.auth.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.facade.AuthFacade;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeUserCredentialUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserLoginUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AuthFacadeTest {

    private AuthFacade authFacade;

    private FakeUserCredentialUseCase userCredentialUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeUserLoginUseCase userLoginUseCase;

    @BeforeEach
    void init() {
        userCredentialUseCase = new FakeUserCredentialUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        userLoginUseCase = new FakeUserLoginUseCase();

        authFacade = new AuthFacade(
                userCredentialUseCase,
                authenticationUseCase,
                userLoginUseCase
        );
    }

    private User buildUser(String userGuid) {
        SignupUserCommand command = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST).skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1).build();
        return User.createGeneralUser(CreateUserCommand.generalUserCreateCommand(command, userGuid));
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

    @Test
    @DisplayName("이메일_로그인_성공_후_최종_로그인_일시가_업데이트된다")
    void login_updatesLastLoginDateTime() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        authFacade.login(loginCommand);

        // then
        assertThat(userLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isTrue();
    }

    @Test
    @DisplayName("토큰_재발급은_최종_로그인_일시를_업데이트하지_않는다")
    void reissueAccessToken_doesNotUpdateLastLoginDateTime() {
        // given
        String oldToken = "old-token";

        // when
        authFacade.reissueAccessToken(oldToken);

        // then
        assertThat(userLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isFalse();
    }

    @Test
    @DisplayName("탈퇴한_유저가_로그인을_시도하면_최종_로그인_일시가_업데이트되지_않는다")
    void login_withdrawnUser_doesNotUpdateLastLoginDateTime() {
        // given
        User withdrawnUser = buildUser(TEST_USER_GUID_1);
        withdrawnUser.withdraw();
        userLoginUseCase.givenUser(withdrawnUser);

        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when, then
        assertThatThrownBy(() -> authFacade.login(loginCommand))
                .isInstanceOf(DomainRuleException.class);

        assertThat(userLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isFalse();
    }
}

