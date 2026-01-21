package teamdevhub.devhub.small.adapter.out.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.auth.AuthenticatedUserAdapter;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.fake.framework.infrastructure.FakeAuthenticationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserAuthenticationAdapterTest {

    private AuthenticatedUserAdapter userAuthenticationAdapter;

    @BeforeEach
    void init() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        FakeAuthenticationManager fakeAuthenticationManager = new FakeAuthenticationManager(authenticatedUser);
        userAuthenticationAdapter = new AuthenticatedUserAdapter(fakeAuthenticationManager);
    }

    @Test
    @DisplayName("이메일과_비밀번호로_AuthenticatedUser_를_가져온다")
    void getAuthenticatedUserByEmailAndPassword() {
        // given, when
        AuthenticatedUser result = userAuthenticationAdapter.getAuthenticatedUser(TEST_EMAIL_1, TEST_PASSWORD_1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.userRole()).isEqualTo(UserRole.USER);
    }
}