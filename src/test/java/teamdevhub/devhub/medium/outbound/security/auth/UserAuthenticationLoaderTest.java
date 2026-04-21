package teamdevhub.devhub.medium.outbound.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import teamdevhub.devhub.core.auth.domain.vo.user.EmailUserCredential;
import teamdevhub.devhub.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.outbound.security.auth.UserAuthenticationLoader;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.port.out.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserAuthenticationLoaderTest {

    private UserAuthenticationLoader userAuthenticationLoader;

    private EmailUserCredentialRepository emailUserCredentialRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        emailUserCredentialRepository = mock(EmailUserCredentialRepository.class);
        userRepository = mock(UserRepository.class);

        userAuthenticationLoader = new UserAuthenticationLoader(emailUserCredentialRepository, userRepository);
    }

    @Test
    @DisplayName("존재하는_이메일이면_UserAuthentication_을_반환한다")
    void returnUserAuthenticationIfEmailExists() {
        // given
        UserCredential userCredential = new UserCredential(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );

        EmailUserCredential emailUserCredential = new EmailUserCredential(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                TEST_PASSWORD_1
        );

        when(emailUserCredentialRepository.findByEmail(TEST_EMAIL_1)).thenReturn(emailUserCredential);
        when(userRepository.findAuthenticatedUserByEmail(TEST_EMAIL_1)).thenReturn(userCredential);

        // when
        UserDetails details = userAuthenticationLoader.loadUserByUsername(TEST_EMAIL_1);

        // then
        assertThat(details).isInstanceOf(UserAuthentication.class);
        UserAuthentication userAuthentication = (UserAuthentication) details;
        assertThat(userAuthentication.getUser()).isEqualTo(userCredential);
        assertThat(userAuthentication.getUsername()).isEqualTo(TEST_USER_GUID_1);
    }

//    @Test
//    @DisplayName("존재하지_않는_이메일이면_UsernameNotFoundException_가_발생한다")
//    void throwUsernameNotFoundExceptionIfEmailNotExists() {
//        // given
//
//        // when
//        when(userRepository.findEmailUserCredentialByEmail("notfound@example.com"))
//                .thenThrow(new UsernameNotFoundException("User not found"));
//
//
//        // then
//        assertThrows(UsernameNotFoundException.class, () -> userAuthenticationLoader.loadUserByUsername("notfound@example.com"));
//        verify(userRepository).findEmailUserCredentialByEmail("notfound@example.com");
//    }
}
