package teamdevhub.devhub.medium.outbound.security.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserAuthenticationTest {

    @Test
    @DisplayName("AuthenticatedUser_를_Wrapping_해서_UserDetails_가_반환된다")
    void wrapAuthenticatedUserToUserDetails() {
        // given, when
        UserCredential user = new UserCredential(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );
        UserAuthentication authentication = new UserAuthentication(user);

        // then
        assertThat(authentication.getUser()).isEqualTo(user);
        assertThat(authentication.getUsername()).isEqualTo(TEST_USER_GUID_1);

        Collection<?> authorities = authentication.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next())
                .isEqualTo(new SimpleGrantedAuthority(UserRole.USER.getAuthority()));

        assertThat(authentication.isAccountNonExpired()).isTrue();
        assertThat(authentication.isAccountNonLocked()).isTrue();
        assertThat(authentication.isCredentialsNonExpired()).isTrue();
        assertThat(authentication.isEnabled()).isTrue();
        assertThat(authentication.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }
}