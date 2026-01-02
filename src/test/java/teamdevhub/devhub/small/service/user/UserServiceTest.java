package teamdevhub.devhub.small.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.common.exception.BusinessRuleException;
import teamdevhub.devhub.domain.record.mail.EmailCertification;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.service.user.UserService;
import teamdevhub.devhub.small.mock.provider.FakeDateTimeProvider;
import teamdevhub.devhub.small.mock.provider.FakePasswordPolicyProvider;
import teamdevhub.devhub.small.mock.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.small.mock.repository.FakeEmailCertificationRepository;
import teamdevhub.devhub.small.mock.repository.FakeRefreshTokenRepository;
import teamdevhub.devhub.small.mock.repository.FakeUserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static final String TEST_GUID = "a1b2c3d4e5f6g7h8i9j10k11l12m13nF";
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USERNAME = "User";
    private static final String TEST_INTRO = "Hello World";

    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_USERNAME = "AdminUser";
    private static final String ADMIN_PASSWORD = "adminPassword123";

    private static final String VERIFIED_EMAIL_CODE = "123456";
    private static final String UNVERIFIED_EMAIL = "unverified@example.com";
    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";

    private UserService userService;
    private FakeUserRepository fakeUserRepository;
    private FakeEmailCertificationRepository fakeEmailCertificationRepository;
    private FakeRefreshTokenRepository fakeRefreshTokenRepository;
    private FakeDateTimeProvider fakeDateTimeProvider;

    @BeforeEach
    void init() {
        fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_GUID);
        FakePasswordPolicyProvider fakePasswordPolicyProvider = new FakePasswordPolicyProvider();
        fakeUserRepository = new FakeUserRepository();
        fakeRefreshTokenRepository = new FakeRefreshTokenRepository();

        EmailCertification verifiedEmail  = new EmailCertification(
                TEST_EMAIL,
                VERIFIED_EMAIL_CODE,
                fakeDateTimeProvider.now().plusMinutes(5),
                fakeDateTimeProvider.now()
        );

        fakeEmailCertificationRepository = new FakeEmailCertificationRepository(List.of(verifiedEmail), fakeDateTimeProvider);

        userService = new UserService(
                fakeUserRepository,
                fakeEmailCertificationRepository,
                fakeRefreshTokenRepository,
                fakePasswordPolicyProvider,
                fakeUuidIdentifierProvider,
                fakeDateTimeProvider
        );
    }

    @Test
    void signup_success() {
        //given
        SignupCommand command = new SignupCommand(TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_INTRO);

        //when
        User savedUser = userService.signup(command);

        //then
        assertNotNull(savedUser.getUserGuid());
        assertEquals(TEST_EMAIL, savedUser.getEmail());
        assertEquals(TEST_USERNAME, savedUser.getUsername());
        assertTrue(fakeUserRepository.findByUserGuid(savedUser.getUserGuid()).isPresent());
        assertFalse(fakeEmailCertificationRepository.isVerified(TEST_EMAIL));
    }

    @Test
    void signup_failsIfEmailNotVerified() {
        //given
        SignupCommand command = new SignupCommand(UNVERIFIED_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_INTRO);

        //when,then
        assertThrows(BusinessRuleException.class, () -> userService.signup(command));
    }

    @Test
    void withdraw_user_success() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_INTRO);
        fakeUserRepository.save(user);

        //when
        userService.withdrawCurrentUser(TEST_GUID);

        //then
        User updatedUser = fakeUserRepository.findByUserGuid(TEST_GUID).orElseThrow(() -> new AssertionError("User must exist after withdraw"));
        assertTrue(updatedUser.isDeleted());
        assertFalse(fakeRefreshTokenRepository.findByEmail(TEST_EMAIL).isPresent());
    }

    @Test
    void updateLastLoginDate_success() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_INTRO);
        fakeUserRepository.save(user);

        //when
        userService.updateLastLoginDate(TEST_GUID);

        //then
        User updatedUser = fakeUserRepository.findByUserGuid(TEST_GUID).orElseThrow(() -> new AssertionError("User must exist after updateLastLoginDate"));
        assertEquals(fakeDateTimeProvider.now(), updatedUser.getLastLoginDateTime());
    }

    @Test
    void updateProfile_success() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_INTRO);
        fakeUserRepository.save(user);

        //when
        UpdateProfileCommand command = new UpdateProfileCommand(TEST_GUID, NEW_USERNAME, NEW_INTRO);
        userService.updateProfile(command);

        //then
        User updatedUser = fakeUserRepository.findByUserGuid(TEST_GUID).orElseThrow(() -> new AssertionError("User must exist after updateProfile"));
        assertEquals(NEW_USERNAME, updatedUser.getUsername());
        assertEquals(NEW_INTRO, updatedUser.getIntroduction());
    }

    @Test
    void initializeAdminUser_success() {
        //given
        userService.initializeAdminUser(ADMIN_EMAIL, ADMIN_USERNAME, ADMIN_PASSWORD);

        //then
        assertTrue(fakeUserRepository.existsByUserRole(UserRole.ADMIN));
    }
}