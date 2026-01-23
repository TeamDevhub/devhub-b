package teamdevhub.devhub.small.port.in.auth.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.application.service.oauth.vo.OauthCallbackResult;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.oauth.FakeOauthResolveUseCase;
import teamdevhub.devhub.port.in.auth.OauthAuthFacade;

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
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );
        oauthAuthenticationUseCase.setCallbackResult(new OauthCallbackResult(SignupStatus.COMPLETED, TEMP_TOKEN));
        oauthResolveUseCase.setOauthUserResult(OauthUserResult.success(signupCompletedUser));

        // when
        OauthAuthResponseDto oauthAuthResponseDto = oauthAuthFacade.handleOAuthCallback("google", "code123");

        // then
        assertThat(oauthAuthResponseDto.getAccessToken()).isNotNull();
        assertThat(oauthAuthResponseDto.getSignupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(authenticationUseCase.getLastLoginUser()).isNotNull();
    }

    @Test
    @DisplayName("가입되지_않은_유저면_로그인_처리_후_OauthAuthResponseDto.fromCallback_을_반환한다")
    void handleOAuthCallback_RequiresSignup() {
        // given
        oauthAuthenticationUseCase.setCallbackResult(new OauthCallbackResult(SignupStatus.PENDING, TEMP_TOKEN));
        oauthResolveUseCase.setOauthUserResult(null);

        // when
        OauthAuthResponseDto oauthAuthResponseDto = oauthAuthFacade.handleOAuthCallback("google", "code456");

        // then
        assertThat(oauthAuthResponseDto.getAccessToken()).isNull();
        assertThat(oauthAuthResponseDto.getSignupStatus()).isEqualTo(SignupStatus.PENDING);
    }
}
