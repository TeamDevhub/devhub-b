package teamdevhub.devhub.small.core.user.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthResult;
import teamdevhub.devhub.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeUserCredentialUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth.FakeOauthResolveUseCase;

import teamdevhub.devhub.fake.pure.application.port.in.usecase.terms.FakeTermsAgreeUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserSignupUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.verification.FakeVerificationUseCase;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.facade.UserSignupFacade;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupFacadeTest {

    private UserSignupFacade userSignupFacade;

    private FakeUserSignupUseCase userSignupUseCase;
    private FakeTermsAgreeUseCase termsAgreeUseCase;
    private FakeOauthResolveUseCase oauthResolveUseCase;
    private FakeUserCredentialUseCase userCredentialUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeVerificationUseCase verificationUseCase;

    @BeforeEach
    void init() {
        userSignupUseCase = new FakeUserSignupUseCase();
        oauthResolveUseCase = new FakeOauthResolveUseCase();
        termsAgreeUseCase = new FakeTermsAgreeUseCase();
        userCredentialUseCase = new FakeUserCredentialUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        verificationUseCase = new FakeVerificationUseCase();

        userSignupFacade = new UserSignupFacade(
                userSignupUseCase,
                termsAgreeUseCase,
                oauthResolveUseCase,
                userCredentialUseCase,
                authenticationUseCase,
                verificationUseCase
        );
    }

    @Test
    @DisplayName("signup_는_사용자_정보를_정상적으로_반환한다")
    void signupReturnsUser() {
        // given
        SignupUserCommand signupUserCommand = new SignupUserCommand(
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST,
                TEST_TERMS_AGREEMENT_LIST,
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
        SignupOauthUserCommand signupCommand = new SignupOauthUserCommand(TEMP_TOKEN, VerificationProvider.GOOGLE, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, TEST_TERMS_AGREEMENT_LIST);

        // when
        OauthAuthResult oauthAuthResult = userSignupFacade.signupWithOauth(signupCommand);

        // then
        assertThat(oauthAuthResult.accessToken()).isEqualTo("access-token");
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(oauthAuthResult.tempToken()).isNull();
    }
}
