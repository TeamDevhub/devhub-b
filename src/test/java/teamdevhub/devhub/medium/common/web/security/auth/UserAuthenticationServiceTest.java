package teamdevhub.devhub.medium.common.web.security.auth;

import org.junit.jupiter.api.DisplayName;
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
import static teamdevhub.devhub.constant.TestConstant.*;

public class UserAuthenticationServiceTest {

    @Test
    @DisplayName("존재하는_이메일이면_UserAuthentication_을_반환한다")
    void returnUserAuthenticationIfEmailExists() {
        // given
        UserUseCase userUseCase = mock(UserUseCase.class);
        AuthenticatedUser user = new AuthenticatedUser(
                TEST_GUID_1,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );
        when(userUseCase.getUserForLogin(TEST_EMAIL_1)).thenReturn(user);
        UserAuthenticationService service = new UserAuthenticationService(userUseCase);

        // when
        UserDetails details = service.loadUserByUsername(TEST_EMAIL_1);

        // then
        assertThat(details).isInstanceOf(UserAuthentication.class);
        UserAuthentication auth = (UserAuthentication) details;
        assertThat(auth.getUser()).isEqualTo(user);
        assertThat(auth.getUsername()).isEqualTo(TEST_GUID_1);
        assertThat(auth.getPassword()).isEqualTo(TEST_PASSWORD_1);
    }

    @Test
    @DisplayName("존재하지_않는_이메일이면_UsernameNotFoundException_가_발생한다")
    void throwUsernameNotFoundExceptionIfEmailNotExists() {
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
