package teamdevhub.devhub.small.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.oauth.OauthSignupService;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.fake.pure.provider.FakeTokenParseProvider;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class OauthSignupServiceTest {

    private OauthSignupService oauthSignupService;

    private FakeTokenParseProvider tokenParseProvider;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();

        oauthSignupService = new OauthSignupService(tokenParseProvider);
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
        OauthUser oauthUser = oauthSignupService.signupWithOauth(signupOauthUserCommand);

        // then
        assertThat(oauthUser.oauthId()).isEqualTo(tempTokenInfo.oauthId());
    }

    /**
     * 테스트케이스 추가 필요
     */
}
