package teamdevhub.devhub.small.domain.user;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.common.exception.DomainRuleException;
import teamdevhub.devhub.domain.common.AuditInfo;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void createGeneralUser() {
        //when
        User user = User.createGeneralUser("a1b2c3d4e5f6g7h8i9j10k11l12m13nF", "user@example.com", "User", "password123", "Hello world");

        //then
        assertThat(user.getUserGuid()).isEqualTo("a1b2c3d4e5f6g7h8i9j10k11l12m13nF");
        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getUsername()).isEqualTo("User");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(user.getIntroduction()).isEqualTo("Hello world");
        assertThat(user.isDeleted()).isFalse();
        assertThat(user.isBlocked()).isFalse();
        assertThat(user.getMannerDegree()).isEqualTo(36.5);
    }

    @Test
    void createAdminUser() {
        //when
        User adminUser = User.createAdminUser("a1b2c3d4e5f6g7h8i9j10k11l12m13nF", "admin@example.com", "Admin", "adminPass");

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
                "a1b2c3d4e5f6g7h8i9j10k11l12m13nF",
                "user@example.com",
                "password123",
                UserRole.USER,
                "User",
                "Intro",
                50.0,
                false,
                null,
                false,
                now,
                auditInfo
        );

        //then
        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getMannerDegree()).isEqualTo(50.0);
        assertThat(user.getLastLoginDateTime()).isEqualTo(now);
    }

    @Test
    void login() {
        //given
        User user = User.createGeneralUser("a1b2c3d4e5f6g7h8i9j10k11l12m13nF", "user@example.com", "User", "password123", "Hello world");
        LocalDateTime loginTime = LocalDateTime.now();

        //when
        user.login(loginTime);

        //then
        assertThat(user.getLastLoginDateTime()).isEqualTo(loginTime);
    }

    @Test
    void withdraw() {
        //given
        User user = User.createGeneralUser("a1b2c3d4e5f6g7h8i9j10k11l12m13nF", "user@example.com", "User", "password123", "Hello world");

        //when
        user.withdraw();

        //then
        assertThat(user.isDeleted()).isTrue();
        assertThat(user.isBlocked()).isFalse();
    }

    @Test
    void withdrawAlreadyDeleted_throwsException() {
        //given
        User user = User.createGeneralUser("a1b2c3d4e5f6g7h8i9j10k11l12m13nF", "user@example.com", "User", "password123", "Hello world");

        //when
        user.withdraw();

        assertThatThrownBy(user::withdraw)
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("이미 탈퇴한 회원입니다.");
    }

    @Test
    void updateProfile() {
        //given
        User user = User.createGeneralUser("a1b2c3d4e5f6g7h8i9j10k11l12m13nF", "user@example.com", "User", "password123", "Hello world");

        //when
        user.updateProfile("NewName", "New Intro");

        //then
        assertThat(user.getUsername()).isEqualTo("NewName");
        assertThat(user.getIntroduction()).isEqualTo("New Intro");
    }

    @Test
    void updateProfileWithNullOrBlank_ignored() {
        //given
        User user = User.createGeneralUser("a1b2c3d4e5f6g7h8i9j10k11l12m13nF", "user@example.com", "User", "password123", "Hello world");

        //when
        user.updateProfile(null, "   ");

        assertThat(user.getUsername()).isEqualTo("User");
        assertThat(user.getIntroduction()).isEqualTo("Hello world");
    }
}