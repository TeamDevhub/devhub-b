package teamdevhub.devhub.small.core.auth.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthResolveService;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthUserResult;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeUserCredentialRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.TempTokenInfo;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class OauthResolveServiceTest {

    private OauthResolveService oauthResolveService;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeUserCredentialRepository userCredentialRepository;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        userCredentialRepository = new FakeUserCredentialRepository();

        oauthResolveService = new OauthResolveService(tokenParseProvider, userCredentialRepository);
    }

    @Test
    @DisplayName("OAuth_유저가_존재하면_loginAvailable_이_true_이다")
    void findOrRequireSignup_existingUser_loginAvailableIsTrue() {
        // given
        UserCredential existingCredential = UserCredential.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        userCredentialRepository.saveOAuthUserCredential(existingCredential, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        OauthUser oauthUser = new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        // when
        OauthUserResult result = oauthResolveService.findOrRequireSignup(oauthUser);

        // then
        assertThat(result.loginAvailable()).isTrue();
        assertThat(result.userCredential()).isNotNull();
        assertThat(result.userCredential().userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("OAuth_유저가_존재하지_않으면_loginAvailable_이_false_이다")
    void findOrRequireSignup_nonExistingUser_loginAvailableIsFalse() {
        // given
        OauthUser oauthUser = new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        // when
        OauthUserResult result = oauthResolveService.findOrRequireSignup(oauthUser);

        // then
        assertThat(result.loginAvailable()).isFalse();
        assertThat(result.userCredential()).isNull();
    }

    @Test
    @DisplayName("다른_제공자의_동일한_oauthId_는_별도_사용자로_처리된다")
    void findOrRequireSignup_sameOauthIdDifferentProvider_treatedSeparately() {
        // given: GOOGLE 로 가입된 사용자
        UserCredential credential = UserCredential.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        userCredentialRepository.saveOAuthUserCredential(credential, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        OauthUser githubUser = new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GITHUB, TEST_EMAIL_1);

        // when: GITHUB 로 조회
        OauthUserResult result = oauthResolveService.findOrRequireSignup(githubUser);

        // then: 다른 제공자이므로 미가입 상태
        assertThat(result.loginAvailable()).isFalse();
    }

    @Test
    @DisplayName("tempToken_으로_OauthUser_를_추출한다")
    void extractOauthUser_validTempToken_returnsOauthUser() {
        // given
        TempTokenInfo tokenInfo = new TempTokenInfo(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
        tokenParseProvider.givenTempToken(TEMP_TOKEN, tokenInfo);

        SignupOauthUserCommand command = SignupOauthUserCommand.builder()
                .tempToken(TEMP_TOKEN)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        OauthUser oauthUser = oauthResolveService.extractOauthUser(command);

        // then
        assertThat(oauthUser.oauthId()).isEqualTo(TEST_OAUTH_ID_1);
        assertThat(oauthUser.verificationProvider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(oauthUser.email()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("유효하지_않은_tempToken_으로_OauthUser_추출_시_예외가_발생한다")
    void extractOauthUser_invalidTempToken_throwsException() {
        // given: 등록되지 않은 토큰
        SignupOauthUserCommand command = SignupOauthUserCommand.builder()
                .tempToken("invalid-temp-token")
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when, then
        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> oauthResolveService.extractOauthUser(command))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
