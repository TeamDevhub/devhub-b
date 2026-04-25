package teamdevhub.devhub.small.core.auth.domain.vo.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.domain.vo.user.EmailUserCredential;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class EmailUserCredentialTest {

    @Test
    @DisplayName("이메일_사용자_자격증명을_생성하면_올바른_값을_갖는다")
    void create_emailUserCredential_hasCorrectValues() {
        EmailUserCredential credential = new EmailUserCredential(
                TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER
        );

        assertThat(credential.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(credential.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(credential.password()).isEqualTo(TEST_PASSWORD_1);
        assertThat(credential.userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("관리자_역할로_이메일_자격증명을_생성할_수_있다")
    void create_emailUserCredential_withAdminRole() {
        EmailUserCredential credential = new EmailUserCredential(
                ADMIN_USER_GUID_1, ADMIN_EMAIL_1, ADMIN_PASSWORD_1, UserRole.ADMIN
        );

        assertThat(credential.userRole()).isEqualTo(UserRole.ADMIN);
        assertThat(credential.email()).isEqualTo(ADMIN_EMAIL_1);
    }

    @Test
    @DisplayName("동일한_값으로_생성한_EmailUserCredential_은_동등하다")
    void emailUserCredentials_withSameValues_areEqual() {
        EmailUserCredential c1 = new EmailUserCredential(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);
        EmailUserCredential c2 = new EmailUserCredential(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);

        assertThat(c1).isEqualTo(c2);
    }

    @Test
    @DisplayName("값이_다른_EmailUserCredential_은_동등하지_않다")
    void emailUserCredentials_withDifferentValues_areNotEqual() {
        EmailUserCredential c1 = new EmailUserCredential(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);
        EmailUserCredential c2 = new EmailUserCredential(TEST_USER_GUID_2, TEST_EMAIL_2, TEST_PASSWORD_2, UserRole.USER);

        assertThat(c1).isNotEqualTo(c2);
    }
}
