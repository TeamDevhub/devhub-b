package teamdevhub.devhub.small.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.user.application.service.UserSignupService;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.fake.pure.application.provider.FakeEncodedPasswordProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserPositionRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserSkillRepository;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

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
        assertThat(userRepository.findByUserGuid(ADMIN_USER_GUID_1).getUserRole()).isEqualTo(UserRole.ADMIN);
    }

//    @Test
//    @DisplayName("관리자_계정이_이미_존재하면_새로운_계정을_생성하지_않는다")
//    void doNotCreateAdminWhenAlreadyExists() {
//        // given
//        CreateUserCommand adminCreateUserCommand = new CreateUserCommand(ADMIN_USER_GUID_1, VerificationProvider.EMAIL, ADMIN_EMAIL_1, ADMIN_EMAIL_1, ADMIN_PASSWORD_1, ADMIN_USERNAME_1, "", List.of(), List.of());
//        User existedAdminUser = User.createAdminUser(adminCreateUserCommand);
//        userRepository.saveAdminUser(existedAdminUser);
//
//        // when
//        SignupAdminCommand signupAdminCommand = new SignupAdminCommand("new-admin-guid", ADMIN_EMAIL_1, ADMIN_PASSWORD_1, ADMIN_USERNAME_1, "", List.of(), List.of(), VERIFICATION_TARGET_1);
//        userSignupService.initializeAdminUser(signupAdminCommand);
//
//        // then
//        UserCredential savedAdminUser = userRepository.findAuthenticatedUserByUserGuid(ADMIN_USER_GUID_1);
//        assertThat(savedAdminUser).isNotNull();
//        assertThat(savedAdminUser.userGuid()).isEqualTo(ADMIN_USER_GUID_1);
//        assertThat(savedAdminUser.userRole()).isEqualTo(UserRole.ADMIN);
//
//        assertThat(userRepository.findByUserGuid("new-admin-guid")).isNull();
//    }

//    @Test
//    @DisplayName("회원가입에_성공하면_인증_테이블에_해당_사용자의_인증내역이_삭제된다")
//    void deleteEmailVerificationRecordWhenSuccessfulSignup() {
//        // given
//        SignupUserCommand signupUserCommand = new SignupUserCommand(TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, TEST_TERMS_AGREEMENT_LIST, VERIFICATION_TARGET_1);
//
//        // when
//        userSignupService.saveEmailUserInfo(signupUserCommand, TEST_USER_GUID_1);
//
//        // then
//        assertThat(userRepository.wasCalled("saveTerms")).isTrue();
//    }

    @Test
    @DisplayName("회원가입_성공시_유저_포지션과_스킬이_저장된다")
    void signupStoresPositionsAndSkills() {
        // given
        SignupUserCommand signupUserCommand = new SignupUserCommand(
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST,
                TEST_TERMS_AGREEMENT_LIST,
                VERIFICATION_TARGET_1
        );

        // when
        userSignupService.saveEmailUserInfo(signupUserCommand, TEST_USER_GUID_1);
        User persistedUser = userRepository.findByUserGuid(TEST_USER_GUID_1);
        Set<UserPosition> positions = userPositionRepository.findByUserGuid(TEST_USER_GUID_1);
        Set<UserSkill> skills = userSkillRepository.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(persistedUser).isNotNull();
        assertThat(positions).extracting(UserPosition::positionCd).containsExactlyInAnyOrderElementsOf(TEST_POSITION_LIST);
        assertThat(skills).extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrderElementsOf(TEST_SKILL_LIST);
    }

    @Test
    @DisplayName("Oauth_회원가입이_성공하면_포지션과_스킬이_저장된다")
    void signupWithOauthStoresPositionsAndSkills() {
        SignupOauthUserCommand signupOauthUserCommand = new SignupOauthUserCommand(
                TEMP_TOKEN,
                VerificationProvider.GOOGLE,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST,
                TEST_TERMS_AGREEMENT_LIST
        );

        OauthUser oauthUser = new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        userSignupService.saveOAuthUserInfo(signupOauthUserCommand, oauthUser, TEST_USER_GUID_1);

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
        SignupUserCommand signupUserCommand = new SignupUserCommand(UNVERIFIED_EMAIL, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, TEST_TERMS_AGREEMENT_LIST, VERIFICATION_TARGET_1);

        // when
        userSignupService.saveEmailUserInfo(signupUserCommand, TEST_USER_GUID_1);

        // then
        assertThat(userRepository.wasCalled("updateLastLoginDateTime")).isFalse();
    }
}
