package teamdevhub.devhub.small.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.oauth.OauthSignupService;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.fake.pure.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.usecase.user.FakeUserSignupUseCase;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OauthSignupServiceTest {

    private FakeTokenParseProvider tokenParseProvider;
    private FakeUserSignupUseCase userSignupUseCase;

    private OauthSignupService oauthSignupService;

    private static final String TEMP_TOKEN = "temp-token-1";
    private static final String OAUTH_ID = "oauth-id-1";
    private static final VerificationProvider PROVIDER = VerificationProvider.GOOGLE;
    private static final String TEST_EMAIL_1 = "test@email.com";

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        userSignupUseCase = new FakeUserSignupUseCase();
        oauthSignupService = new OauthSignupService(tokenParseProvider, userSignupUseCase);
    }

    @Test
    @DisplayName("Oauth_회원가입_성공하면_tempToken_을_반환한다")
    void signup_with_oauth_success_returns_tempToken() {
        // given
        TempTokenInfo tempTokenInfo = new TempTokenInfo(OAUTH_ID, TokenType.TEMP, SignupStatus.PENDING, PROVIDER, TEST_EMAIL_1);
        tokenParseProvider.givenTempToken(TEMP_TOKEN, tempTokenInfo);

        OauthSignupCommand oauthSignupCommand = OauthSignupCommand.builder()
                .tempToken(TEMP_TOKEN)
                .username("username1")
                .password("password1")
                .introduction("intro1")
                .positionList(List.of("Backend"))
                .skillList(List.of("Java"))
                .build();

        // when
        String resultToken = oauthSignupService.signupWithOauth(oauthSignupCommand);

        // then
        assertThat(resultToken).isEqualTo(TEMP_TOKEN);
        assertThat(userSignupUseCase.isSignupCalled()).isTrue();
        assertThat(userSignupUseCase.getLastOauthSignupCommand()).isEqualTo(oauthSignupCommand);
        assertThat(userSignupUseCase.getLastOauthUser().oauthId()).isEqualTo(OAUTH_ID);
        assertThat(userSignupUseCase.getLastOauthUser().email()).isEqualTo(TEST_EMAIL_1);
        assertThat(userSignupUseCase.getLastOauthUser().verificationProvider()).isEqualTo(PROVIDER);
    }
}
