package teamdevhub.devhub.small.core.auth.domain.vo.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.domain.vo.user.OAuthUserCredential;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class OauthUserCredentialTest {

    @Test
    @DisplayName("OAuth_사용자_자격증명을_생성하면_올바른_값을_갖는다")
    void create_oauthUserCredential_hasCorrectValues() {
        OAuthUserCredential credential = new OAuthUserCredential(
                TEST_USER_GUID_1, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1, UserRole.USER
        );

        assertThat(credential.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(credential.provider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(credential.oauthId()).isEqualTo(TEST_OAUTH_ID_1);
        assertThat(credential.userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("다양한_OAuth_제공자로_자격증명을_생성할_수_있다")
    void create_oauthUserCredential_withDifferentProviders() {
        for (VerificationProvider provider : new VerificationProvider[]{
                VerificationProvider.GOOGLE, VerificationProvider.GITHUB,
                VerificationProvider.KAKAO, VerificationProvider.NAVER}) {

            OAuthUserCredential credential = new OAuthUserCredential(
                    TEST_USER_GUID_1, provider, TEST_OAUTH_ID_1, UserRole.USER
            );

            assertThat(credential.provider()).isEqualTo(provider);
        }
    }

    @Test
    @DisplayName("동일한_값으로_생성한_OAuthUserCredential_은_동등하다")
    void oauthUserCredentials_withSameValues_areEqual() {
        OAuthUserCredential c1 = new OAuthUserCredential(TEST_USER_GUID_1, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1, UserRole.USER);
        OAuthUserCredential c2 = new OAuthUserCredential(TEST_USER_GUID_1, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1, UserRole.USER);

        assertThat(c1).isEqualTo(c2);
    }

    @Test
    @DisplayName("제공자가_다른_OAuthUserCredential_은_동등하지_않다")
    void oauthUserCredentials_withDifferentProvider_areNotEqual() {
        OAuthUserCredential google = new OAuthUserCredential(TEST_USER_GUID_1, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1, UserRole.USER);
        OAuthUserCredential github = new OAuthUserCredential(TEST_USER_GUID_1, VerificationProvider.GITHUB, TEST_OAUTH_ID_1, UserRole.USER);

        assertThat(google).isNotEqualTo(github);
    }
}
