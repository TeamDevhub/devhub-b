package teamdevhub.devhub.medium.adapter.out.infrastructure.persistence.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import teamdevhub.devhub.adapter.out.auth.AuthenticatedUserAdapter;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.web.security.auth.UserAuthentication;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest
public class AuthenticatedUserAdapterMediumTest {

    @Autowired
    private AuthenticatedUserAdapter authenticatedUserAdapter;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("이메일과_비밀번호로_AuthenticatedUser_를_조회한다")
    void getAuthenticatedUserByEmailAndPassword() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        Authentication authentication = mock(Authentication.class);
        given(authentication.getPrincipal()).willReturn(userAuthentication);

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);

        // when
        AuthenticatedUser result = authenticatedUserAdapter.getAuthenticatedUser(TEST_EMAIL_1, TEST_PASSWORD_1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.userRole()).isEqualTo(UserRole.USER);
    }
}
