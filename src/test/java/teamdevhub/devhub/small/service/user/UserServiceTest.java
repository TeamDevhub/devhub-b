package teamdevhub.devhub.small.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.common.exception.BusinessRuleException;
import teamdevhub.devhub.domain.common.record.auth.RefreshToken;
import teamdevhub.devhub.domain.common.record.mail.EmailCertification;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.record.UserPosition;
import teamdevhub.devhub.domain.user.record.UserSkill;
import teamdevhub.devhub.service.user.UserService;
import teamdevhub.devhub.small.mock.provider.FakeDateTimeProvider;
import teamdevhub.devhub.small.mock.provider.FakePasswordPolicyProvider;
import teamdevhub.devhub.small.mock.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.small.mock.repository.FakeEmailCertificationRepository;
import teamdevhub.devhub.small.mock.repository.FakeRefreshTokenRepository;
import teamdevhub.devhub.small.mock.repository.FakeUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USERNAME = "User";
    private static final String TEST_INTRO = "Hello World";

    private static final List<String> TEST_POSITION_LIST = List.of("001");
    private static final List<String> TEST_SKILL_LIST = List.of("001");
    private static final Set<UserPosition> TEST_POSITIONS = Set.of(new UserPosition("001"));
    private static final Set<UserSkill> TEST_SKILLS = Set.of(new UserSkill("001"));

    private static final String ADMIN_GUID = "ADMINa1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_USERNAME = "AdminUser";
    private static final String ADMIN_PASSWORD = "adminPassword123";

    private static final String EMAIL_CODE = "123456";
    private static final String UNVERIFIED_EMAIL = "unverified@example.com";

    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";
    private static final List<String> NEW_POSITION_LIST = List.of("002");
    private static final List<String> NEW_SKILL_LIST = List.of("002");
    private static final Set<UserPosition> NEW_POSITIONS = Set.of(new UserPosition("002"));
    private static final Set<UserSkill> NEW_SKILLS = Set.of(new UserSkill("002"));

    private UserService userService;
    private FakeUserRepository fakeUserRepository;
    private FakeEmailCertificationRepository fakeEmailCertificationRepository;
    private FakeUuidIdentifierProvider fakeUuidIdentifierProvider;
    private FakePasswordPolicyProvider fakePasswordPolicyProvider;
    private FakeRefreshTokenRepository fakeRefreshTokenRepository;
    private FakeDateTimeProvider fakeDateTimeProvider;

    @BeforeEach
    void init() {
        fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_GUID);
        fakePasswordPolicyProvider = new FakePasswordPolicyProvider();
        fakeUserRepository = new FakeUserRepository();
        fakeRefreshTokenRepository = new FakeRefreshTokenRepository();

        EmailCertification verifiedEmail  = new EmailCertification(
                TEST_EMAIL,
                EMAIL_CODE,
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
    void 관리자_계정을_생성한다() {
        //given
        FakeUuidIdentifierProvider adminUuidProvider = new FakeUuidIdentifierProvider(ADMIN_GUID);
        userService = new UserService(
                fakeUserRepository,
                fakeEmailCertificationRepository,
                fakeRefreshTokenRepository,
                fakePasswordPolicyProvider,
                adminUuidProvider,
                fakeDateTimeProvider
        );

        //when
        userService.initializeAdminUser(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        //then
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_GUID)).isNotNull();
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_GUID).getUsername()).isEqualTo(ADMIN_USERNAME);
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_GUID).getPassword()).isEqualTo(fakePasswordPolicyProvider.encode(ADMIN_PASSWORD));
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_GUID).getUserRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void 인증_인가_관련_사용자_정보를_조회한다() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        fakeUserRepository.saveNewUser(user);

        //when
        userService.getUserForAuth(TEST_EMAIL);

        //then
        assertThat(fakeUserRepository.findUserByEmailForAuth(TEST_EMAIL)).isNotNull();
        assertThat(fakeUserRepository.findUserByEmailForAuth(TEST_EMAIL).userGuid()).isEqualTo(TEST_GUID);
        assertThat(fakeUserRepository.findUserByEmailForAuth(TEST_EMAIL).email()).isEqualTo(TEST_EMAIL);
        assertThat(fakeUserRepository.findUserByEmailForAuth(TEST_EMAIL).userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void 회원가입에_성공하면_이메일_인증_테이블에_해당_유저의_인증내역이_삭제된다() {
        //given
        SignupCommand signupCommand = new SignupCommand(TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        //when
        User savedUser = userService.signup(signupCommand);

        //then
        assertThat(fakeEmailCertificationRepository.hasUnexpiredCode(signupCommand.getEmail())).isFalse();
        assertThat(fakeUserRepository.saveNewUser(savedUser).getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(fakeUserRepository.saveNewUser(savedUser).getPositions()).isEqualTo(TEST_POSITIONS);
        assertThat(fakeUserRepository.saveNewUser(savedUser).getSkills()).isEqualTo(TEST_SKILLS);
        assertThat(fakeUserRepository.saveNewUser(savedUser).getPassword()).isEqualTo(fakePasswordPolicyProvider.encode(TEST_PASSWORD));
    }

    @Test
    void 이메일_인증이_완료되지_않은_사용자가_회원가입을_요청하면_예외를_던진다() {
        //given
        SignupCommand signupCommand = new SignupCommand(UNVERIFIED_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        //when
        assertThatThrownBy(
                () -> userService.signup(signupCommand))
                //then
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("E-mail 인증에 실패했습니다.");
    }

    @Test
    void 회원가입_후_로그인_하지_않은_사용자의_최종_로그인_일시는_존재하지_않는다() {
        //given
        SignupCommand signupCommand = new SignupCommand(TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        //when
        userService.signup(signupCommand);

        //then
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID).getLastLoginDateTime()).isNull();
    }

    @Test
    void 로그인을_하면_최종_로그인_일시가_변한다() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        fakeUserRepository.saveNewUser(user);

        // when
        userService.updateLastLoginDateTime(TEST_GUID);

        //then
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID).getLastLoginDateTime()).isNotNull();
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID).getLastLoginDateTime()).isEqualTo(fakeDateTimeProvider.now());
    }

    @Test
    void 회원_상세_정보를_조회했을_때_모든_정보가_조회된다() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        fakeUserRepository.saveNewUser(user);

        //when, then
        assertThat(userService.getCurrentUserProfile(user.getUserGuid())).isNotNull();
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getUsername()).isEqualTo(user.getUsername());
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getIntroduction()).isEqualTo(user.getIntroduction());
    }

    @Test
    void 회원정보를_수정하면_수정한_값으로_변경된다() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        fakeUserRepository.saveNewUser(user);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_GUID, NEW_USERNAME, NEW_INTRO, NEW_POSITION_LIST, NEW_SKILL_LIST);
        userService.updateProfile(updateProfileCommand);

        //then
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID).getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID).getIntroduction()).isEqualTo(NEW_INTRO);
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID).getPositions()).isEqualTo(NEW_POSITIONS);
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID).getSkills()).isEqualTo(NEW_SKILLS);
    }

    @Test
    void 회원탈퇴한_사용자의_deleted_값은_true_이다() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        fakeUserRepository.saveNewUser(user);
        RefreshToken refreshToken = new RefreshToken(TEST_EMAIL, "testToken");
        fakeRefreshTokenRepository.save(refreshToken);

        // when
        userService.withdrawCurrentUser(TEST_GUID);

        //then
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID).isDeleted()).isTrue();
        assertThat(fakeRefreshTokenRepository.findByEmail(TEST_EMAIL)).isNull();
    }

    @Test
    void 일반_사용자는_USER_권한이_존재한다() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        fakeUserRepository.saveNewUser(user);

        //when, then
        assertThat(userService.existsByUserRole(UserRole.USER)).isTrue();
        assertThat(userService.existsByUserRole(UserRole.ADMIN)).isFalse();
    }
}