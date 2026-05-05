package teamdevhub.devhub.small.core.auth.domain.vo.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.domain.OAuthUserCredential;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_OAUTH_ID_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

class OAuthAuthenticatedUserTest {

    @Test
    @DisplayName("OAuth_사용자_자격증명을_생성하면_올바른_값을_갖는다")
    void create_oauthUserCredential_hasCorrectValues() {
        OAuthUserCredential oauthUserCredential = new OAuthUserCredential(
                TEST_USER_GUID_1, TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, UserRole.USER
        );

        assertThat(oauthUserCredential.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(oauthUserCredential.getVerificationProvider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(oauthUserCredential.getOauthId()).isEqualTo(TEST_OAUTH_ID_1);
        assertThat(oauthUserCredential.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("다양한_OAuth_제공자로_자격증명을_생성할_수_있다")
    void create_oauthUserCredential_withDifferentProviders() {
        for (VerificationProvider verificationProvider : new VerificationProvider[]{
                VerificationProvider.GOOGLE, VerificationProvider.GITHUB,
                VerificationProvider.KAKAO, VerificationProvider.NAVER}) {

            OAuthUserCredential oauthUserCredential = new OAuthUserCredential(
                    TEST_USER_GUID_1, TEST_OAUTH_ID_1, verificationProvider, UserRole.USER
            );

            assertThat(oauthUserCredential.getVerificationProvider()).isEqualTo(verificationProvider);
        }
    }
}
