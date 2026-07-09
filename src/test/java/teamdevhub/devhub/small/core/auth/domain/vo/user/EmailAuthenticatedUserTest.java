package teamdevhub.devhub.small.core.auth.domain.vo.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class EmailAuthenticatedUserTest {

    @Test
    @DisplayName("이메일_사용자_자격증명을_생성하면_올바른_값을_갖는다")
    void create_emailUserCredential_hasCorrectValues() {
        EmailUserCredential emailUserCredential =EmailUserCredential.of(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);

        assertThat(emailUserCredential.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(emailUserCredential.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(emailUserCredential.getPassword()).isEqualTo(TEST_PASSWORD_1);
        assertThat(emailUserCredential.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("관리자_역할로_이메일_자격증명을_생성할_수_있다")
    void create_emailUserCredential_withAdminRole() {
        EmailUserCredential emailUserCredential = EmailUserCredential.of(ADMIN_USER_GUID_1, ADMIN_EMAIL_1, ADMIN_PASSWORD_1, UserRole.ADMIN);

        assertThat(emailUserCredential.getUserRole()).isEqualTo(UserRole.ADMIN);
        assertThat(emailUserCredential.getEmail()).isEqualTo(ADMIN_EMAIL_1);
    }
}
