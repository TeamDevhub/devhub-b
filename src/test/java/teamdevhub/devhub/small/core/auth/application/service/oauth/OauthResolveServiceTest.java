package teamdevhub.devhub.small.core.auth.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthResolveService;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthUserResult;
import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.TempTokenInfo;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class OauthResolveServiceTest {

    private OauthResolveService oauthResolveService;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeUserRepository userRepository;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        userRepository = new FakeUserRepository();

        oauthResolveService = new OauthResolveService(tokenParseProvider, userRepository);
    }

    @Test
    @DisplayName("OAuth_유저가_존재하면_반환된_OauthUserResult_의_loginAvailable_값은_true_이다")
    void returnOauthResultSuccessWhenUserExists() {
        // given
        TempTokenInfo tokenInfo = new TempTokenInfo(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
        tokenParseProvider.givenTempToken(TEMP_TOKEN, tokenInfo);

        OauthUser oauthUser = new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
        SignupOauthUserCommand signupOauthUserCommand = SignupOauthUserCommand.builder()
                .tempToken(TEMP_TOKEN)
                .username(TEST_USERNAME_1)
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        CreateUserCommand oauthCreateUserCommand = CreateUserCommand.oauthUserCreateCommand(signupOauthUserCommand, oauthUser, TEST_USER_GUID_1);
        User createdOauthUser = User.createOauthUser(oauthCreateUserCommand);
        userRepository.save(createdOauthUser);

        // when
        OauthUserResult oauthUserResult = oauthResolveService.findOrRequireSignup(oauthUser);

        // then
        assertThat(oauthUserResult.loginAvailable()).isTrue();
        assertThat(oauthUserResult.authenticatedUser().userGuid()).isEqualTo(createdOauthUser.getUserGuid());
    }

    @Test
    @DisplayName("OAuth_유저가_존재하지_않으면_반환된_OauthUserResult_의_loginAvailable_값은_false_이다")
    void returnOauthResultRequiresSignupWhenUserNotExists() {
        // given
        OauthUser oauthUser = new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        // when
        OauthUserResult oauthUserResult = oauthResolveService.findOrRequireSignup(oauthUser);

        // then
        assertThat(oauthUserResult.loginAvailable()).isFalse();
        assertThat(oauthUserResult.authenticatedUser()).isNull();
    }

    @Test
    @DisplayName("Oauth_회원가입_성공하면_tempToken_을_반환한다")
    void signup_with_oauth_success_returns_tempToken() {
        // given
        TempTokenInfo tempTokenInfo = new TempTokenInfo(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
        tokenParseProvider.givenTempToken(TEMP_TOKEN, tempTokenInfo);

        SignupOauthUserCommand signupOauthUserCommand = SignupOauthUserCommand.builder()
                .tempToken(TEMP_TOKEN)
                .username(TEST_USERNAME_1)
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        OauthUser oauthUser = oauthResolveService.extractOauthUser(signupOauthUserCommand);

        // then
        assertThat(oauthUser.oauthId()).isEqualTo(tempTokenInfo.oauthId());
    }
}
