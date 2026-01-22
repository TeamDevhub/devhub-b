package teamdevhub.devhub.small.port.in.auth.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.application.service.oauth.vo.OauthCallbackResult;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthResolveUseCase;
import teamdevhub.devhub.port.in.auth.OauthAuthFacade;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthAuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.usecase.OauthResolveUseCase;

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
    @DisplayName("provider_를_받으면_createOAuthAuthorizationUrl_로_리다이렉트_URL_을_리턴받을_수_있다.")
    void createOAuthAuthorizationUrlDelegates() {
        // when
        String url = oauthAuthFacade.createOAuthAuthorizationUrl("google");

        // then
        assertThat(url).isEqualTo("https://oauth.test/google");
    }

    /**
     * 테스트케이스 보완 필요
    @Test
    @DisplayName("가입된_유저면_로그인_처리_후_OauthAuthResponseDto.loggedIn_을_반환한다.")
    void handleOAuthCallbackLoginForCompletedUser() {
        // given
        OauthCallbackResult oauthCallbackResult = new OauthCallbackResult(SignupStatus.COMPLETED, TEMP_TOKEN);
        FakeOauthAuthenticationUseCase fakeOauthAuthenticationUseCase = new FakeOauthAuthenticationUseCase();

        AuthenticatedUser authenticatedUser = new AuthenticatedUser("user-1", SignupStatus.COMPLETED, "email@test.com", "password", "ROLE_USER");
        OauthUserResult oauthUserResult = OauthUserResult.success(authenticatedUser);
        FakeOauthResolveUseCase fakeResolveUseCase = new FakeOauthResolveUseCase(oauthUserResult);

        FakeAuthenticationUseCase fakeAuthenticationUseCase = new FakeAuthenticationUseCase();

        OauthAuthFacade facade = new OauthAuthFacade(fakeOauthAuthenticationUseCase, fakeResolveUseCase, fakeAuthenticationUseCase);

        // when
        OauthAuthResponseDto response = facade.handleOAuthCallback("google", "code123");

        // then
        assertThat(response.isLoggedIn()).isTrue();
        assertThat(fakeAuthenticationUseCase.getLastLogin()).isNotNull();
        assertThat(fakeAuthenticationUseCase.getLastLogin().getSignupStatus()).isEqualTo(SignupStatus.COMPLETED);
    }

    @Test
    @DisplayName("가입되지_않은_유저면_로그인_처리_후_OauthAuthResponseDto.fromCallback_을_반환한다.")
    void handleOAuthCallback_RequiresSignup() {
        // given
        String tempToken = "TEMP456";
        OauthCallbackResult callbackResult = new OauthCallbackResult(SignupStatus.PENDING, tempToken);
        FakeOauthAuthenticationUseCase fakeAuthUseCase = new FakeOauthAuthenticationUseCase(callbackResult);

        FakeOauthResolveUseCase fakeResolveUseCase = new FakeOauthResolveUseCase(null);

        FakeAuthenticationUseCase fakeAuthenticationUseCase = new FakeAuthenticationUseCase();

        OauthAuthFacade facade = new OauthAuthFacade(fakeAuthUseCase, fakeResolveUseCase, fakeAuthenticationUseCase);

        // when
        OauthAuthResponseDto response = facade.handleOAuthCallback("google", "code456");

        // then
        assertThat(response.isLoggedIn()).isFalse();
        assertThat(response.getSignupStatus()).isEqualTo(SignupStatus.PENDING);
    }
    */
}
