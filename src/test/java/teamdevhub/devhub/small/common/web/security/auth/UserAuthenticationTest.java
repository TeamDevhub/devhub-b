package teamdevhub.devhub.small.common.web.security.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import teamdevhub.devhub.common.web.security.auth.UserAuthentication;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

public class UserAuthenticationTest {

    @Test
    void AuthenticatedUser_를_Wrapping_해서_UserDetails_가_반환된다() {
        // given, when
        AuthenticatedUser user = new AuthenticatedUser(
                TEST_GUID,
                TEST_EMAIL,
                TEST_PASSWORD,
                UserRole.USER
        );
        UserAuthentication authentication = new UserAuthentication(user);

        // then
        assertThat(authentication.getUser()).isEqualTo(user);
        assertThat(authentication.getUsername()).isEqualTo(TEST_GUID);
        assertThat(authentication.getPassword()).isEqualTo(TEST_PASSWORD);

        Collection<?> authorities = authentication.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next())
                .isEqualTo(new SimpleGrantedAuthority(UserRole.USER.getAuthority()));

        assertThat(authentication.isAccountNonExpired()).isTrue();
        assertThat(authentication.isAccountNonLocked()).isTrue();
        assertThat(authentication.isCredentialsNonExpired()).isTrue();
        assertThat(authentication.isEnabled()).isTrue();
        assertThat(authentication.getUserGuid()).isEqualTo(TEST_GUID);
    }
}