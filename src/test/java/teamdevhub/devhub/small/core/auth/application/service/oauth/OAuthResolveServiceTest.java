package teamdevhub.devhub.small.core.auth.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.oauth.OAuthResolveService;
import teamdevhub.devhub.core.auth.application.service.oauth.vo.OAuthUserResult;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeUserCredentialRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.TempTokenInfo;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class OAuthResolveServiceTest {

    private OAuthResolveService oauthResolveService;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeUserCredentialRepository userCredentialRepository;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        userCredentialRepository = new FakeUserCredentialRepository();

        oauthResolveService = new OAuthResolveService(tokenParseProvider, userCredentialRepository);
    }

    @Test
    @DisplayName("OAuth_유저가_존재하면_loginAvailable_이_true_이다")
    void findOrRequireSignup_existingUser_loginAvailableIsTrue() {
        // given
        AuthenticatedUser existingCredential = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        userCredentialRepository.saveOAuthUserCredential(existingCredential, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        OAuthUser oauthUser = new OAuthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        // when
        OAuthUserResult result = oauthResolveService.findOrRequireSignup(oauthUser);

        // then
        assertThat(result.loginAvailable()).isTrue();
        assertThat(result.authenticatedUser()).isNotNull();
        assertThat(result.authenticatedUser().userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("OAuth_유저가_존재하지_않으면_loginAvailable_이_false_이다")
    void findOrRequireSignup_nonExistingUser_loginAvailableIsFalse() {
        // given
        OAuthUser oauthUser = new OAuthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        // when
        OAuthUserResult result = oauthResolveService.findOrRequireSignup(oauthUser);

        // then
        assertThat(result.loginAvailable()).isFalse();
        assertThat(result.authenticatedUser()).isNull();
    }

    @Test
    @DisplayName("다른_제공자의_동일한_oauthId_는_별도_사용자로_처리된다")
    void findOrRequireSignup_sameOAuthIdDifferentProvider_treatedSeparately() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        userCredentialRepository.saveOAuthUserCredential(authenticatedUser, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        OAuthUser githubUser = new OAuthUser(TEST_OAUTH_ID_1, VerificationProvider.GITHUB, TEST_EMAIL_1);

        // when
        OAuthUserResult result = oauthResolveService.findOrRequireSignup(githubUser);

        // then
        assertThat(result.loginAvailable()).isFalse();
    }

    @Test
    @DisplayName("tempToken_으로_OAuthUser_를_추출한다")
    void extractOAuthUser_validTempToken_returnsOAuthUser() {
        // given
        TempTokenInfo tokenInfo = new TempTokenInfo(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
        tokenParseProvider.givenTempToken(TEMP_TOKEN, tokenInfo);

        SignupOAuthUserCommand signupOAuthUserCommand = SignupOAuthUserCommand.builder()
                .tempToken(TEMP_TOKEN)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        OAuthUser oauthUser = oauthResolveService.extractOAuthUser(signupOAuthUserCommand);

        // then
        assertThat(oauthUser.oauthId()).isEqualTo(TEST_OAUTH_ID_1);
        assertThat(oauthUser.verificationProvider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(oauthUser.email()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("유효하지_않은_tempToken_으로_OAuthUser_추출_시_예외가_발생한다")
    void extractOAuthUser_invalidTempToken_throwsException() {
        // given
        SignupOAuthUserCommand signupOAuthUserCommand = SignupOAuthUserCommand.builder()
                .tempToken("invalid-temp-token")
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when, then
        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> oauthResolveService.extractOAuthUser(signupOAuthUserCommand))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
