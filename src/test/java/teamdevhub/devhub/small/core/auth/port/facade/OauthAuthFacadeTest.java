package teamdevhub.devhub.small.core.auth.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthResult;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthorizationResult;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthUserResult;
import teamdevhub.devhub.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth.FakeOauthAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth.FakeOauthResolveUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserLoginUseCase;
import teamdevhub.devhub.core.auth.port.in.facade.OauthAuthFacade;
import teamdevhub.devhub.core.common.exception.DomainRuleException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class OauthAuthFacadeTest {

    private OauthAuthFacade oauthAuthFacade;

    private FakeOauthAuthenticationUseCase oauthAuthenticationUseCase;
    private FakeOauthResolveUseCase oauthResolveUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeUserLoginUseCase userLoginUseCase;

    @BeforeEach
    void init() {
        oauthAuthenticationUseCase = new FakeOauthAuthenticationUseCase();
        oauthResolveUseCase = new FakeOauthResolveUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        userLoginUseCase = new FakeUserLoginUseCase();

        oauthAuthFacade = new OauthAuthFacade(oauthAuthenticationUseCase, oauthResolveUseCase, authenticationUseCase, userLoginUseCase);
    }

    @Test
    @DisplayName("provider_를_받으면_createOAuthAuthorizationUrl_로_리다이렉트_URL_과_state_를_리턴받을_수_있다")
    void createOAuthAuthorizationUrlDelegates() {
        // when
        OauthAuthorizationResult result = oauthAuthFacade.createOAuthAuthorizationUrl("google");

        // then
        assertThat(result.url()).isEqualTo("https://oauth.test/google");
        assertThat(result.state()).isEqualTo("test-state");
    }

    @Test
    @DisplayName("가입된_유저면_로그인_처리_후_OauthAuthResponseDto.loggedIn_을_반환한다")
    void handleOAuthCallbackLoginForCompletedUser() {
        // given
        AuthenticatedUser signupCompletedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );
        oauthResolveUseCase.setOauthUserResult(OauthUserResult.success(signupCompletedUser));

        // when
        OauthAuthResult oauthAuthResult = oauthAuthFacade.handleOAuthCallback("google", "code123");

        // then
        assertThat(oauthAuthResult.accessToken()).isNotNull();
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(authenticationUseCase.getLastLoginUser()).isNotNull();
    }

    @Test
    @DisplayName("OAuth_로그인_성공_후_최종_로그인_일시가_업데이트된다")
    void handleOAuthCallbackUpdatesLastLoginDateTime() {
        // given
        AuthenticatedUser signupCompletedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );
        oauthResolveUseCase.setOauthUserResult(OauthUserResult.success(signupCompletedUser));

        // when
        oauthAuthFacade.handleOAuthCallback("google", "code123");

        // then
        assertThat(userLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isTrue();
    }

    @Test
    @DisplayName("탈퇴한_유저가_OAuth_로그인을_시도하면_예외가_발생한다")
    void handleOAuthCallback_withdrawnUser_throwsException() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST).skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1).build();
        User withdrawnUser = User.createGeneralUser(CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1));
        withdrawnUser.withdraw();
        userLoginUseCase.givenUser(withdrawnUser);

        AuthenticatedUser withdrawnAuthUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        oauthResolveUseCase.setOauthUserResult(OauthUserResult.success(withdrawnAuthUser));

        // when, then
        assertThatThrownBy(() -> oauthAuthFacade.handleOAuthCallback("google", "code789"))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("가입되지_않은_유저면_로그인_처리_후_OauthAuthResponseDto.fromCallback_을_반환한다")
    void handleOAuthCallbackRequiresSignup() {
        // given
        oauthResolveUseCase.setOauthUserResult(OauthUserResult.requiresSignup());

        // when
        OauthAuthResult oauthAuthResult = oauthAuthFacade.handleOAuthCallback("google", "code456");

        // then
        assertThat(oauthAuthResult.accessToken()).isNull();
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.PENDING);
    }
}
