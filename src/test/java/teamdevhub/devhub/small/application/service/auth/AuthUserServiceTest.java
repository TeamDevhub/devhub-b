package teamdevhub.devhub.small.application.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.auth.AuthUserService;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.fake.pure.provider.FakePasswordPolicyProvider;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AuthUserServiceTest {

    private AuthUserService authUserService;

    private FakeUserRepository fakeUserRepository;
    private FakePasswordPolicyProvider fakePasswordPolicyProvider;
    private FakeUuidIdentifierProvider fakeUuidIdentifierProvider;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        fakePasswordPolicyProvider = new FakePasswordPolicyProvider();
        fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_USER_GUID_1);

        authUserService = new AuthUserService(
                fakeUserRepository,
                fakePasswordPolicyProvider,
                fakeUuidIdentifierProvider
        );
    }

    @Test
    @DisplayName("관리자_계정을_생성한다")
    void createAdminAccount() {
        // given
        FakeUuidIdentifierProvider fakeAdminUuidIdentifierProvider = new FakeUuidIdentifierProvider(ADMIN_USER_GUID);
        authUserService = new AuthUserService(
                fakeUserRepository,
                fakePasswordPolicyProvider,
                fakeAdminUuidIdentifierProvider
        );

        // when
        authUserService.initializeAdminUser(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        // then
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_USER_GUID)).isNotNull();
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_USER_GUID).getUserGuid()).isEqualTo(ADMIN_USER_GUID);
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_USER_GUID).getUsername()).isEqualTo(ADMIN_USERNAME);
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_USER_GUID).getPassword()).isEqualTo(fakePasswordPolicyProvider.encode(ADMIN_PASSWORD));
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_USER_GUID).getUserRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("인증_인가_관련_사용자_정보를_조회한다")
    void fetchUserInfoForAuthentication() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        authUserService.getUserForLogin(TEST_EMAIL_1);

        // then
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(user.getEmail())).isNotNull();
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(user.getEmail()).userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(user.getEmail()).userRole()).isEqualTo(UserRole.USER);
    }

}
