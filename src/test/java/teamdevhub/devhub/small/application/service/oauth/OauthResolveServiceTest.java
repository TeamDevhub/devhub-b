package teamdevhub.devhub.small.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.oauth.OauthResolveService;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.fake.pure.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;

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
        TempTokenInfo tokenInfo = new TempTokenInfo(TEST_OAUTH_ID_1, TokenType.TEMP, SignupStatus.COMPLETED, VerificationProvider.GOOGLE, TEST_EMAIL_1);
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

        UserCreateCommand oauthUserCreateCommand = UserCreateCommand.oauthUserCreateCommand(signupOauthUserCommand, oauthUser, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User createdOauthUser = User.createOauthUser(oauthUserCreateCommand);
        createdOauthUser.completeSignup();
        userRepository.save(createdOauthUser);

        ResolveOauthUserCommand resolveOauthUserCommand = new ResolveOauthUserCommand(TEMP_TOKEN);

        // when
        OauthUserResult oauthUserResult = oauthResolveService.resolveOauthUser(resolveOauthUserCommand);

        // then
        assertThat(oauthUserResult.loginAvailable()).isTrue();
        assertThat(oauthUserResult.authenticatedUser().userGuid()).isEqualTo(createdOauthUser.getUserGuid());
    }

    @Test
    @DisplayName("OAuth_유저가_존재하면_반환된_OauthUserResult_의_loginAvailable_값은_false_이다")
    void returnOauthResultRequiresSignupWhenUserNotExists() {
        // given
        TempTokenInfo tokenInfo = new TempTokenInfo(TEST_OAUTH_ID_1, TokenType.TEMP, SignupStatus.COMPLETED, VerificationProvider.GOOGLE, TEST_EMAIL_1);
        tokenParseProvider.givenTempToken(TEMP_TOKEN, tokenInfo);

        ResolveOauthUserCommand resolveOauthUserCommand = new ResolveOauthUserCommand(TEMP_TOKEN);

        // when
        OauthUserResult oauthUserResult = oauthResolveService.resolveOauthUser(resolveOauthUserCommand);

        // then
        assertThat(oauthUserResult.loginAvailable()).isFalse();
        assertThat(oauthUserResult.authenticatedUser()).isNull();
    }

    @Test
    @DisplayName("Oauth_회원가입_성공하면_tempToken_을_반환한다")
    void signup_with_oauth_success_returns_tempToken() {
        // given
        TempTokenInfo tempTokenInfo = new TempTokenInfo(TEST_OAUTH_ID_1, TokenType.TEMP, SignupStatus.PENDING, VerificationProvider.GOOGLE, TEST_EMAIL_1);
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
