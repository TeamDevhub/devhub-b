package teamdevhub.devhub.small.domain.user;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.common.exception.DomainRuleException;
import teamdevhub.devhub.domain.common.AuditInfo;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    private static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USERNAME = "User";
    private static final String TEST_INTRO = "Hello World";

    private static final List<String> TEST_POSITIONS = List.of("001");
    private static final List<String> TEST_SKILLS = List.of("001");

    private static final String ADMIN_GUID = "ADMINa1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_USERNAME = "AdminUser";
    private static final String ADMIN_PASSWORD = "adminPassword123";

    private static final String VERIFIED_EMAIL_CODE = "123456";
    private static final String UNVERIFIED_EMAIL = "unverified@example.com";

    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";
    private static final List<String> NEW_POSITIONS = List.of("002");
    private static final List<String> NEW_SKILLS = List.of("002");

    @Test
    void createGeneralUser() {
        //when
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITIONS, TEST_SKILLS);

        //then
        assertThat(user.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(user.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO);
        assertThat(user.isDeleted()).isFalse();
        assertThat(user.isBlocked()).isFalse();
        assertThat(user.getMannerDegree()).isEqualTo(36.5);
    }

    @Test
    void createAdminUser() {
        //when
        User adminUser = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        //then
        assertThat(adminUser.getUserRole()).isEqualTo(UserRole.ADMIN);
        assertThat(adminUser.isDeleted()).isFalse();
        assertThat(adminUser.isBlocked()).isFalse();
    }

    @Test
    void of() {
        //given
        LocalDateTime now = LocalDateTime.now();
        AuditInfo auditInfo = AuditInfo.empty();

        //when
        User user = User.of(
                TEST_GUID,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_USERNAME,
                UserRole.USER,
                TEST_INTRO,
                TEST_POSITIONS,
                TEST_SKILLS,
                50.0,
                false,
                null,
                false,
                now,
                auditInfo
        );

        //then
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(user.getMannerDegree()).isEqualTo(50.0);
        assertThat(user.getLastLoginDateTime()).isEqualTo(now);
    }

    @Test
    void login() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITIONS, TEST_SKILLS);
        LocalDateTime loginTime = LocalDateTime.now();

        //when
        user.login(loginTime);

        //then
        assertThat(user.getLastLoginDateTime()).isEqualTo(loginTime);
    }

    @Test
    void withdraw() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITIONS, TEST_SKILLS);

        //when
        user.withdraw();

        //then
        assertThat(user.isDeleted()).isTrue();
        assertThat(user.isBlocked()).isFalse();
    }

    @Test
    void withdrawAlreadyDeleted_throwsException() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITIONS, TEST_SKILLS);

        //when
        user.withdraw();

        assertThatThrownBy(user::withdraw)
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("이미 탈퇴한 회원입니다.");
    }

    @Test
    void updateProfile() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITIONS, TEST_SKILLS);

        //when
        user.updateProfile(NEW_USERNAME, NEW_INTRO);

        //then
        assertThat(user.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(user.getIntroduction()).isEqualTo(NEW_INTRO);
    }

    @Test
    void updateProfileWithNullOrBlank_ignored() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITIONS, TEST_SKILLS);

        //when
        user.updateProfile(null, "   ");

        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO);
    }
}