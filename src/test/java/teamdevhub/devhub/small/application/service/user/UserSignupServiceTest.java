package teamdevhub.devhub.small.application.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.user.UserSignupService;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.UserRole;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.fake.pure.provider.FakeEncodedPasswordProvider;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserPositionRepository;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserSkillRepository;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.user.command.SignupAdminCommand;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupServiceTest {

    private UserSignupService userSignupService;

    private FakeEncodedPasswordProvider encodedPasswordProvider;
    private FakeUserRepository userRepository;
    private FakeUserPositionRepository userPositionRepository;
    private FakeUserSkillRepository userSkillRepository;

    @BeforeEach
    void init() {
        encodedPasswordProvider = new FakeEncodedPasswordProvider();
        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_USER_GUID_1);
        userRepository = new FakeUserRepository();
        userPositionRepository = new FakeUserPositionRepository();
        userSkillRepository = new FakeUserSkillRepository();

        userSignupService = new UserSignupService(
                encodedPasswordProvider,
                fakeUuidIdentifierProvider,
                userRepository,
                userPositionRepository,
                userSkillRepository
        );
    }

    @Test
    @DisplayName("관리자_계정을_생성한다")
    void createAdminAccount() {
        // given
        FakeUuidIdentifierProvider fakeAdminUuidIdentifierProvider = new FakeUuidIdentifierProvider(ADMIN_USER_GUID_1);
        userSignupService = new UserSignupService(
                encodedPasswordProvider,
                fakeAdminUuidIdentifierProvider,
                userRepository,
                userPositionRepository,
                userSkillRepository
        );

        SignupAdminCommand signupAdminCommand = new SignupAdminCommand(null, ADMIN_EMAIL_1, ADMIN_PASSWORD_1, ADMIN_USERNAME_1, "", List.of(), List.of(), null);
        // when
        userSignupService.initializeAdminUser(signupAdminCommand);

        // then
        assertThat(userRepository.findByUserGuid(ADMIN_USER_GUID_1).getUserGuid()).isEqualTo(ADMIN_USER_GUID_1);
        assertThat(userRepository.findByUserGuid(ADMIN_USER_GUID_1).getPassword()).isEqualTo(encodedPasswordProvider.encode(ADMIN_PASSWORD_1));
        assertThat(userRepository.findByUserGuid(ADMIN_USER_GUID_1).getUserRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("관리자_계정이_이미_존재하면_새로운_계정을_생성하지_않는다")
    void doNotCreateAdminWhenAlreadyExists() {
        // given
        CreateUserCommand adminCreateUserCommand = new CreateUserCommand(ADMIN_USER_GUID_1, VerificationProvider.EMAIL, ADMIN_EMAIL_1, ADMIN_EMAIL_1, ADMIN_PASSWORD_1, ADMIN_USERNAME_1, "", List.of(), List.of());
        User existedAdminUser = User.createAdminUser(adminCreateUserCommand);
        userRepository.saveAdminUser(existedAdminUser);

        // when
        SignupAdminCommand signupAdminCommand = new SignupAdminCommand("new-admin-guid", ADMIN_EMAIL_1, ADMIN_PASSWORD_1, ADMIN_USERNAME_1, "", List.of(), List.of(), VERIFICATION_TARGET_1);
        userSignupService.initializeAdminUser(signupAdminCommand);

        // then
        AuthenticatedUser savedAdminUser = userRepository.findAuthenticatedUserByUserGuid(ADMIN_USER_GUID_1);
        assertThat(savedAdminUser).isNotNull();
        assertThat(savedAdminUser.userGuid()).isEqualTo(ADMIN_USER_GUID_1);
        assertThat(savedAdminUser.userRole()).isEqualTo(UserRole.ADMIN);

        assertThat(userRepository.findByUserGuid("new-admin-guid")).isNull();
    }

    @Test
    @DisplayName("회원가입에_성공하면_인증_테이블에_해당_사용자의_인증내역이_삭제된다")
    void deleteEmailVerificationRecordWhenSuccessfulSignup() {
        // given
        SignupUserCommand signupUserCommand = new SignupUserCommand(null, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, VERIFICATION_TARGET_1);

        // when
        userSignupService.signup(signupUserCommand);

        // then
        assertThat(userRepository.wasCalled("save")).isTrue();
        assertThat(userRepository.findByUserGuid(TEST_USER_GUID_1).getPassword()).isEqualTo(encodedPasswordProvider.encode(TEST_PASSWORD_1));
    }

    @Test
    @DisplayName("회원가입_성공시_유저_포지션과_스킬이_저장된다")
    void signupStoresPositionsAndSkills() {
        // given
        SignupUserCommand signupUserCommand = new SignupUserCommand(
                null,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST,
                VERIFICATION_TARGET_1
        );

        // when
        userSignupService.signup(signupUserCommand);
        User persistedUser = userRepository.findByUserGuid(TEST_USER_GUID_1);
        Set<UserPosition> positions = userPositionRepository.findByUserGuid(TEST_USER_GUID_1);
        Set<UserSkill> skills = userSkillRepository.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(persistedUser).isNotNull();
        assertThat(persistedUser.getPassword()).isEqualTo(encodedPasswordProvider.encode(TEST_PASSWORD_1));
        assertThat(positions).extracting(UserPosition::positionCd).containsExactlyInAnyOrderElementsOf(TEST_POSITION_LIST);
        assertThat(skills).extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrderElementsOf(TEST_SKILL_LIST);
    }

    @Test
    @DisplayName("Oauth_회원가입이_성공하면_포지션과_스킬이_저장된다")
    void signupWithOauthStoresPositionsAndSkills() {
        SignupOauthUserCommand signupOauthUserCommand = new SignupOauthUserCommand(
                TEMP_TOKEN,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST
        );

        OauthUser oauthUser = new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        userSignupService.signupWithOauth(signupOauthUserCommand, oauthUser);

        Set<UserPosition> positions = userPositionRepository.findByUserGuid(TEST_USER_GUID_1);
        Set<UserSkill> skills = userSkillRepository.findByUserGuid(TEST_USER_GUID_1);
        User persistedUser = userRepository.findByUserGuid(TEST_USER_GUID_1);

        assertThat(positions).extracting(UserPosition::positionCd).containsExactlyInAnyOrderElementsOf(TEST_POSITION_LIST);
        assertThat(skills).extracting(UserSkill::skillCd).containsExactlyInAnyOrderElementsOf(TEST_SKILL_LIST);
        assertThat(persistedUser).isNotNull();
        assertThat(persistedUser.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("회원가입_후_로그인_하지_않은_사용자의_최종_로그인_일시는_존재하지_않는다")
    void haveNoLastLoginDateForUserWhoHasNotLoggedInAfterSignup() {
        // given
        SignupUserCommand signupUserCommand = new SignupUserCommand(null, UNVERIFIED_EMAIL, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, VERIFICATION_TARGET_1);

        // when
        userSignupService.signup(signupUserCommand);

        // then
        assertThat(userRepository.wasCalled("updateLastLoginDateTime")).isFalse();
    }
}
