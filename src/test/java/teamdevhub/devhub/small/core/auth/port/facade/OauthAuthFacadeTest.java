package teamdevhub.devhub.small.core.auth.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthResult;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthUserResult;
import teamdevhub.devhub.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth.FakeOauthAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth.FakeOauthResolveUseCase;
import teamdevhub.devhub.core.auth.port.in.facade.OauthAuthFacade;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class OauthAuthFacadeTest {

    private OauthAuthFacade oauthAuthFacade;

    private FakeOauthAuthenticationUseCase oauthAuthenticationUseCase;
    private FakeOauthResolveUseCase oauthResolveUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;

    @BeforeEach
    void init() {
        oauthAuthenticationUseCase = new FakeOauthAuthenticationUseCase();
        oauthResolveUseCase = new FakeOauthResolveUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();

        oauthAuthFacade = new OauthAuthFacade(oauthAuthenticationUseCase, oauthResolveUseCase, authenticationUseCase);
    }

    @Test
    @DisplayName("provider_를_받으면_createOAuthAuthorizationUrl_로_리다이렉트_URL_을_리턴받을_수_있다")
    void createOAuthAuthorizationUrlDelegates() {
        // when
        String redirectUrl = oauthAuthFacade.createOAuthAuthorizationUrl("google");

        // then
        assertThat(redirectUrl).isEqualTo("https://oauth.test/google");
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
