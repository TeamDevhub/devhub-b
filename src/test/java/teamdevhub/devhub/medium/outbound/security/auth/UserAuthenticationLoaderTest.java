package teamdevhub.devhub.medium.outbound.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.outbound.security.auth.UserAuthenticationLoader;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserAuthenticationLoaderTest {

    private UserAuthenticationLoader userAuthenticationLoader;

    private EmailUserCredentialRepository emailUserCredentialRepository;

    @BeforeEach
    public void init() {
        emailUserCredentialRepository = mock(EmailUserCredentialRepository.class);
        userAuthenticationLoader = new UserAuthenticationLoader(emailUserCredentialRepository);
    }

    @Test
    @DisplayName("존재하는_이메일이면_UserAuthentication_을_반환한다")
    void returnUserAuthenticationIfEmailExists() {
        // given
        EmailUserCredential emailUserCredential = EmailUserCredential.of(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        when(emailUserCredentialRepository.findByEmail(TEST_EMAIL_1))
                .thenReturn(Optional.of(emailUserCredential));

        // when
        UserDetails details = userAuthenticationLoader.loadUserByUsername(TEST_EMAIL_1);

        // then
        assertThat(details).isInstanceOf(UserAuthentication.class);

        UserAuthentication userAuthentication = (UserAuthentication) details;

        AuthenticatedUser expected = AuthenticatedUser.of(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );

        assertThat(userAuthentication.getUser()).isEqualTo(expected);
        assertThat(userAuthentication.getUsername()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("존재하지_않는_이메일이면_UsernameNotFoundException_발생")
    void throwExceptionIfEmailNotExists() {
        // given
        when(emailUserCredentialRepository.findByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                UsernameNotFoundException.class,
                () -> userAuthenticationLoader.loadUserByUsername("notfound@example.com")
        );
    }
}