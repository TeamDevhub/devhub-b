package teamdevhub.devhub.small.core.auth.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthenticationService;
import teamdevhub.devhub.fake.pure.oauth.FakeOauthClient;
import teamdevhub.devhub.fake.pure.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.fake.pure.selector.FakeOauthClientSelector;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OauthAuthenticationServiceTest {

    private OauthAuthenticationService oauthAuthenticationService;

    private FakeOauthClient oauthClient;

    @BeforeEach
    void init() {
        oauthClient = new FakeOauthClient();
        FakeOauthClientSelector fakeOauthClientSelector = new FakeOauthClientSelector(oauthClient);
        FakeTokenIssueProvider tokenIssueProvider = new FakeTokenIssueProvider();

        oauthAuthenticationService = new OauthAuthenticationService(tokenIssueProvider, fakeOauthClientSelector);
    }

    @Test
    @DisplayName("Oauth_인증_제공자에_따른_리다이렉트_URL_을_생성할_수_있다")
    void handleOAuthCallbackCreateAuthorizationUrlWithProvider() {
        // given
        String provider = "google";

        // when
        String redirectAuthorizationUrl = oauthAuthenticationService.createAuthorizationUrl(provider);

        // then
        assertThat(redirectAuthorizationUrl).isEqualTo("https://oauth.test/authorize/");
    }
}
