package teamdevhub.devhub.small.adapter.out.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.auth.SpringSecurityAuthenticationAdapter;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.small.mock.infrastructure.FakeAuthenticationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

class SpringSecurityAuthenticationAdapterTest {

    private SpringSecurityAuthenticationAdapter authenticationAdapter;

    @BeforeEach
    void init() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_GUID,
                TEST_EMAIL,
                TEST_PASSWORD,
                UserRole.USER
        );

        FakeAuthenticationManager fakeAuthenticationManager = new FakeAuthenticationManager(authenticatedUser);

        authenticationAdapter = new SpringSecurityAuthenticationAdapter(fakeAuthenticationManager);
    }

    @Test
    void 이메일과_비밀번호로_AuthenticatedUser_를_가져온다() {
        // given, when
        AuthenticatedUser result = authenticationAdapter.getAuthenticatedUser(TEST_EMAIL, TEST_PASSWORD);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userGuid()).isEqualTo(TEST_GUID);
        assertThat(result.email()).isEqualTo(TEST_EMAIL);
        assertThat(result.userRole()).isEqualTo(UserRole.USER);
    }
}