package teamdevhub.devhub.small.core.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.auth.domain.OAuthUserCredential;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserCredentialTest {

    @Test
    @DisplayName("이메일_크리덴셜을_생성하면_모든_필드가_설정된다")
    void createEmailCredential_allFieldsSet() {
        // when
        EmailUserCredential credential = new EmailUserCredential(
                TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);

        // then
        assertThat(credential.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(credential.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(credential.getPassword()).isEqualTo(TEST_PASSWORD_1);
        assertThat(credential.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("비밀번호가_일치하면_verifyPassword를_호출해도_예외가_발생하지_않는다")
    void verifyPassword_passwordMatches_noException() {
        // given
        EmailUserCredential credential = new EmailUserCredential(
                TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);

        // when, then — no exception
        credential.verifyPassword(true);
    }

    @Test
    @DisplayName("비밀번호가_일치하지_않으면_verifyPassword_호출시_USER_PASSWORD_FAIL_예외가_발생한다")
    void verifyPassword_passwordNotMatches_throwsDomainRuleException() {
        // given
        EmailUserCredential credential = new EmailUserCredential(
                TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);

        // when, then
        assertThatThrownBy(() -> credential.verifyPassword(false))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_PASSWORD_FAIL.getMessage());
    }

    @Test
    @DisplayName("changePassword를_호출하면_비밀번호가_새로운_값으로_변경된다")
    void changePassword_updatesPassword() {
        // given
        EmailUserCredential credential = new EmailUserCredential(
                TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);

        // when
        credential.changePassword("newEncodedPassword");

        // then
        assertThat(credential.getPassword()).isEqualTo("newEncodedPassword");
    }

    @Test
    @DisplayName("OAuth_크리덴셜을_생성하면_모든_필드가_설정된다")
    void createOAuthCredential_allFieldsSet() {
        // when
        OAuthUserCredential credential = OAuthUserCredential.builder()
                .userGuid(TEST_USER_GUID_1)
                .oauthId(TEST_OAUTH_ID_1)
                .verificationProvider(VerificationProvider.GOOGLE)
                .userRole(UserRole.USER)
                .build();

        // then
        assertThat(credential.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(credential.getOauthId()).isEqualTo(TEST_OAUTH_ID_1);
        assertThat(credential.getVerificationProvider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(credential.getUserRole()).isEqualTo(UserRole.USER);
    }
}
