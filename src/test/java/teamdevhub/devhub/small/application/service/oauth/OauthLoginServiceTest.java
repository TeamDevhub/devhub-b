package teamdevhub.devhub.small.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.application.service.oauth.OauthLoginService;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.fake.pure.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.command.OauthLoginCommand;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class OauthLoginServiceTest {

    private FakeTokenParseProvider tokenParseProvider;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeUserRepository userRepository;

    private OauthLoginService oauthLoginService;

    private static final String TEMP_TOKEN = "temp-token";
    private static final String OAUTH_ID = "oauth-id-123";
    private static final VerificationProvider PROVIDER = VerificationProvider.GOOGLE;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        authenticationUseCase = new FakeAuthenticationUseCase();
        userRepository = new FakeUserRepository();

        oauthLoginService = new OauthLoginService(tokenParseProvider, authenticationUseCase, userRepository);
    }

    @Test
    @DisplayName("OAuth 유저가 존재하면 로그인에 성공한다")
    void login_with_oauth_success_when_user_exists() {
        // given
        TempTokenInfo tokenInfo = new TempTokenInfo(OAUTH_ID, TokenType.TEMP, SignupStatus.COMPLETED, PROVIDER, TEST_EMAIL_1);
        tokenParseProvider.givenTempToken(TEMP_TOKEN, tokenInfo);

        OauthUser oauthUser = new OauthUser(OAUTH_ID, PROVIDER, TEST_EMAIL_1);
        OauthSignupCommand oauthSignupCommand = OauthSignupCommand.builder()
                .tempToken(TEMP_TOKEN)
                .username(TEST_USERNAME_1)
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        UserCreateCommand oauthUserCreateCommand = UserCreateCommand.oauthUserCreateCommand(oauthSignupCommand, oauthUser, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User createdOauthUser = User.createOauthUser(oauthUserCreateCommand);
        createdOauthUser.completeSignup();
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(createdOauthUser.getUserGuid(), createdOauthUser.getSignupStatus(), createdOauthUser.getEmail(), createdOauthUser.getPassword(), createdOauthUser.getUserRole());
        userRepository.save(createdOauthUser);

        OauthLoginCommand oauthLoginCommand = new OauthLoginCommand(TEMP_TOKEN);

        // when
        LoginResponseDto loginResponseDto = oauthLoginService.loginWithOauth(oauthLoginCommand);

        // then
        assertThat(loginResponseDto.getSignupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(authenticationUseCase.getLastLoginUser()).isEqualTo(authenticatedUser);
    }

    @Test
    @DisplayName("OAuth 유저가 존재하지 않으면 회원가입 대기 상태를 반환한다")
    void login_with_oauth_returns_pending_when_user_not_exists() {
        // given
        TempTokenInfo tokenInfo = new TempTokenInfo(OAUTH_ID, TokenType.TEMP, SignupStatus.COMPLETED, PROVIDER,TEST_EMAIL_1);
        tokenParseProvider.givenTempToken(TEMP_TOKEN, tokenInfo);

        OauthLoginCommand oauthLoginCommand = new OauthLoginCommand(TEMP_TOKEN);

        // when
        LoginResponseDto loginResponseDto = oauthLoginService.loginWithOauth(oauthLoginCommand);

        // then
        assertThat(loginResponseDto.getSignupStatus()).isEqualTo(SignupStatus.PENDING);
        assertThat(loginResponseDto.getTempToken()).isEqualTo(TEMP_TOKEN);
    }
}

