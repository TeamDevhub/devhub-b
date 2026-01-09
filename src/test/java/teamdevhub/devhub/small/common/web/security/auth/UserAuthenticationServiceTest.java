package teamdevhub.devhub.small.common.web.security.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import teamdevhub.devhub.common.web.security.auth.UserAuthentication;
import teamdevhub.devhub.common.web.security.auth.UserAuthenticationService;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.port.in.user.UserUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

public class UserAuthenticationServiceTest {

    @Test
    void 존재하는_이메일이면_UserAuthentication_을_반환한다() {
        // given
        UserUseCase userUseCase = mock(UserUseCase.class);
        AuthenticatedUser user = new AuthenticatedUser(
                TEST_GUID,
                TEST_EMAIL,
                TEST_PASSWORD,
                UserRole.USER
        );
        when(userUseCase.getUserForLogin(TEST_EMAIL)).thenReturn(user);
        UserAuthenticationService service = new UserAuthenticationService(userUseCase);

        // when
        UserDetails details = service.loadUserByUsername(TEST_EMAIL);

        // then
        assertThat(details).isInstanceOf(UserAuthentication.class);
        UserAuthentication auth = (UserAuthentication) details;
        assertThat(auth.getUser()).isEqualTo(user);
        assertThat(auth.getUsername()).isEqualTo(TEST_GUID);
        assertThat(auth.getPassword()).isEqualTo(TEST_PASSWORD);
    }

    @Test
    void 존재하지_않는_이메일이면_UsernameNotFoundException_가_발생한다() {
        // given
        UserUseCase userUseCase = mock(UserUseCase.class);

        // when
        when(userUseCase.getUserForLogin("notfound@example.com"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        UserAuthenticationService service = new UserAuthenticationService(userUseCase);

        // then
        assertThrows(UsernameNotFoundException.class, () ->
                service.loadUserByUsername("notfound@example.com")
        );

        verify(userUseCase).getUserForLogin("notfound@example.com");
    }
}
