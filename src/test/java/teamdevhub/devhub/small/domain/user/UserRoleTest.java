package teamdevhub.devhub.small.domain.user;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.domain.user.UserRole;

import static org.assertj.core.api.Assertions.assertThat;

class UserRoleTest {

    @Test
    void ADMIN_역할은_ROLE_ADMIN_권한을_가진다() {
        // given, when
        UserRole userRole = UserRole.ADMIN;

        // then
        assertThat(userRole.getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void USER_역할은_ROLE_USER_권한을_가진다() {
        // given, when
        UserRole userRole = UserRole.USER;


        // then
        assertThat(userRole.getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    void getAuthority_상수_값이_UserRole과_일치한다() {
        // given, when
        assertThat(UserRole.ADMIN.getAuthority())
                // then
                .isEqualTo(UserRole.Authority.ADMIN);

        // given, when
        assertThat(UserRole.USER.getAuthority())
                // then
                .isEqualTo(UserRole.Authority.USER);
    }
}