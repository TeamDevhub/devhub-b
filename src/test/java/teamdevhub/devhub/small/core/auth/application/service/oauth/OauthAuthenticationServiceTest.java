package teamdevhub.devhub.small.core.auth.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthenticationService;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthorizationResult;
import teamdevhub.devhub.fake.pure.application.port.out.auth.oauth.FakeOauthClient;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.pure.application.selector.FakeOauthClientSelector;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class OauthAuthenticationServiceTest {

    private OauthAuthenticationService oauthAuthenticationService;

    private FakeOauthClient oauthClient;

    @BeforeEach
    void init() {
        oauthClient = new FakeOauthClient();
        FakeOauthClientSelector fakeOauthClientSelector = new FakeOauthClientSelector(oauthClient);
        FakeTokenIssueProvider tokenIssueProvider = new FakeTokenIssueProvider();
        FakeUuidIdentifierProvider identifierProvider = new FakeUuidIdentifierProvider(TEST_USER_GUID_1);

        oauthAuthenticationService = new OauthAuthenticationService(tokenIssueProvider, fakeOauthClientSelector, identifierProvider);
    }

    @Test
    @DisplayName("Oauth_인증_제공자에_따른_리다이렉트_URL_을_생성할_수_있다")
    void handleOAuthCallbackCreateAuthorizationUrlWithProvider() {
        // given
        String provider = "google";

        // when
        OauthAuthorizationResult result = oauthAuthenticationService.createAuthorizationUrl(provider);

        // then
        assertThat(result.url()).isEqualTo("https://oauth.test/authorize/");
        assertThat(result.state()).isEqualTo(TEST_USER_GUID_1);
    }
}
