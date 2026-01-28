package teamdevhub.devhub.small.core.auth.application.selector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.core.auth.application.selector.oauth.CompositeOauthClientSelector;
import teamdevhub.devhub.shared.exception.BusinessRuleException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.fake.pure.application.port.out.auth.oauth.StubOauthClient;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CompositeOauthClientSelectorTest {

    private CompositeOauthClientSelector compositeOauthClientSelector;

    @BeforeEach
    void init() {
        OauthClient githubOauthClient = new StubOauthClient(VerificationProvider.GITHUB);
        OauthClient googleOauthClient = new StubOauthClient(VerificationProvider.GOOGLE);

        compositeOauthClientSelector = new CompositeOauthClientSelector(List.of(githubOauthClient, googleOauthClient));
    }

    @Test
    @DisplayName("GOOGLE_OAUTH_이면_GOOGLE_OauthClient_가_선택된다")
    void select_githubProvider_returnsGithubClient() {
        // given, when
        OauthClient selectedOauClient = compositeOauthClientSelector.select(VerificationProvider.GOOGLE);

        // then
        assertThat(selectedOauClient.supports(VerificationProvider.GOOGLE)).isTrue();
    }

    @Test
    @DisplayName("지원하지_않는_Provider_이면_BusinessRuleException_이_발생한다")
    void select_unsupportedProvider_throwsException() {
        // given, when, then
        assertThatThrownBy(
                () -> compositeOauthClientSelector.select(VerificationProvider.KAKAO))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.OAUTH_FAIL.getMessage());
    }
}