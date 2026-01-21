package teamdevhub.devhub.small.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthCallbackResponseDto;
import teamdevhub.devhub.application.service.oauth.OauthCallbackService;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.fake.pure.oauth.FakeOauthClient;
import teamdevhub.devhub.fake.pure.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.selector.FakeOauthClientSelector;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class OauthCallbackServiceTest {

    private OauthCallbackService oauthCallbackService;

    private FakeUserRepository userRepository;
    private FakeOauthClient oauthClient;

    @BeforeEach
    void init() {
        oauthClient = new FakeOauthClient();
        FakeOauthClientSelector fakeOauthClientSelector = new FakeOauthClientSelector(oauthClient);
        FakeTokenIssueProvider tokenIssueProvider = new FakeTokenIssueProvider();
        userRepository = new FakeUserRepository();

        oauthCallbackService = new OauthCallbackService(tokenIssueProvider, fakeOauthClientSelector, userRepository);
    }

    @Test
    @DisplayName("Oauth_인증_제공자에_따른_리다이렉트_URL_을_생성할_수_있다")
    void handleOAuthCallback_createAuthorizationUrl_with_provider() {
        // given
        String provider = "google";

        // when
        String redirectAuthorizationUrl = oauthCallbackService.createAuthorizationUrl(provider);

        // then
        assertThat(redirectAuthorizationUrl).isEqualTo("https://oauth.test/authorize/");
    }

    @Test
    @DisplayName("기존_OAuth_유저면_COMPLETED_상태의_토큰이_발급된다")
    void handleOAuthCallback_completed_whenUserExists() {
        // given
        OauthUser oauthUser = new OauthUser("oauth-id", VerificationProvider.GOOGLE, "test@test.com");
        oauthClient.withOauthUser(oauthUser);

        OauthSignupCommand oauthSignupCommand = OauthSignupCommand.builder()
                .tempToken("tempToken")
                .username(TEST_USERNAME_1)
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();
        UserCreateCommand oauthUserCreateCommand = UserCreateCommand.oauthUserCreateCommand(oauthSignupCommand, oauthUser, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User createdOauthUser = User.createOauthUser(oauthUserCreateCommand);
        userRepository.save(createdOauthUser);

        // when
        OauthCallbackResponseDto oauthCallbackResponseDto = oauthCallbackService.handleOAuthCallback(VerificationProvider.GOOGLE, "authorization-code");

        // then
        assertThat(oauthCallbackResponseDto.signupStatus()).isEqualTo(SignupStatus.COMPLETED);
    }

    @Test
    @DisplayName("신규_OAuth_유저면_PENDING_상태의_토큰이_발급된다")
    void handleOAuthCallback_pending_whenUserNotExists() {
        // given
        OauthUser oauthUser = new OauthUser("oauth-id", VerificationProvider.GOOGLE, "test@test.com");
        oauthClient.withOauthUser(oauthUser);

        // when
        OauthCallbackResponseDto oauthCallbackResponseDto = oauthCallbackService.handleOAuthCallback(VerificationProvider.GOOGLE, "authorization-code");

        // then
        assertThat(oauthCallbackResponseDto.signupStatus()).isEqualTo(SignupStatus.PENDING);
    }
}
