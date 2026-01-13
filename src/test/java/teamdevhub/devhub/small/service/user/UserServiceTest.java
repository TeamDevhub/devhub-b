package teamdevhub.devhub.small.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.domain.mail.EmailVerification;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.domain.vo.auth.RefreshToken;
import teamdevhub.devhub.fake.pure.provider.FakeDateTimeProvider;
import teamdevhub.devhub.fake.pure.provider.FakePasswordPolicyProvider;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.pure.repository.*;
import teamdevhub.devhub.fake.pure.usecase.FakeEmailVerificationUseCase;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.service.exception.BusinessRuleException;
import teamdevhub.devhub.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.TestConstant.*;

class UserServiceTest {

    private UserService userService;
    private FakeUserRepository fakeUserRepository;
    private FakeUserPositionRepository fakeUserPositionRepository;
    private FakeUserSkillRepository fakeUserSkillRepository;
    private FakeEmailVerificationUseCase fakeEmailVerificationUseCase;
    private FakeEmailVerificationRepository fakeEmailVerificationRepository;
    private FakePasswordPolicyProvider fakePasswordPolicyProvider;
    private FakeRefreshTokenRepository fakeRefreshTokenRepository;
    private FakeDateTimeProvider fakeDateTimeProvider;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        fakeUserPositionRepository = new FakeUserPositionRepository();
        fakeUserSkillRepository = new FakeUserSkillRepository();

        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_GUID_1);
        fakePasswordPolicyProvider = new FakePasswordPolicyProvider();
        fakeRefreshTokenRepository = new FakeRefreshTokenRepository();
        fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));

        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL_1, EMAIL_CODE, fakeDateTimeProvider.now().plusMinutes(5));
        emailVerification.verify(EMAIL_CODE, fakeDateTimeProvider.now());
        fakeEmailVerificationRepository = new FakeEmailVerificationRepository(List.of(emailVerification), fakeDateTimeProvider);
        fakeEmailVerificationUseCase = new FakeEmailVerificationUseCase(fakeEmailVerificationRepository, fakeDateTimeProvider);
        userService = new UserService(
                fakeUserRepository,
                fakeUserPositionRepository,
                fakeUserSkillRepository,
                fakeEmailVerificationUseCase,
                fakeEmailVerificationRepository,
                fakeRefreshTokenRepository,
                fakePasswordPolicyProvider,
                fakeUuidIdentifierProvider,
                fakeDateTimeProvider
        );
    }

    @Test
    @DisplayName("관리자_계정을_생성한다")
    void createAdminAccount() {
        // given
        FakeUuidIdentifierProvider adminUuidProvider = new FakeUuidIdentifierProvider(ADMIN_GUID);
        userService = new UserService(
                fakeUserRepository,
                fakeUserPositionRepository,
                fakeUserSkillRepository,
                fakeEmailVerificationUseCase,
                fakeEmailVerificationRepository,
                fakeRefreshTokenRepository,
                fakePasswordPolicyProvider,
                adminUuidProvider,
                fakeDateTimeProvider
        );

        // when
        userService.initializeAdminUser(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        // then
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_GUID)).isNotNull();
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_GUID).getUsername()).isEqualTo(ADMIN_USERNAME);
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_GUID).getPassword()).isEqualTo(fakePasswordPolicyProvider.encode(ADMIN_PASSWORD));
        assertThat(fakeUserRepository.findByUserGuid(ADMIN_GUID).getUserRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("인증_인가_관련_사용자_정보를_조회한다")
    void fetchUserInfoForAuthentication() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        userService.getUserForLogin(TEST_EMAIL_1);

        // then
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(TEST_EMAIL_1)).isNotNull();
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(TEST_EMAIL_1).userGuid()).isEqualTo(TEST_GUID_1);
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(TEST_EMAIL_1).email()).isEqualTo(TEST_EMAIL_1);
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(TEST_EMAIL_1).userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("회원가입에_성공하면_이메일_인증_테이블에_해당_유저의_인증내역이_삭제된다")
    void deleteEmailVerificationRecordWhenSuccessfulSignup() {
        // given
        SignupCommand signupCommand = new SignupCommand(TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        User savedUser = userService.signup(signupCommand);

        // then
        assertThat(fakeEmailVerificationRepository.existUnexpiredCode(signupCommand.getEmail())).isFalse();
        assertThat(fakeUserRepository.save(savedUser).getUserGuid()).isEqualTo(TEST_GUID_1);
        assertThat(fakeUserRepository.save(savedUser).getPassword()).isEqualTo(fakePasswordPolicyProvider.encode(TEST_PASSWORD_1));
    }

    @Test
    @DisplayName("이메일_인증이_완료되지_않은_사용자가_회원가입을_요청하면_예외를_던진다")
    void throwExceptionWhenSignupWithoutEmailVerification() {
        // given
        SignupCommand signupCommand = new SignupCommand(UNVERIFIED_EMAIL, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        assertThatThrownBy(
                () -> userService.signup(signupCommand))
                // then
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("E-mail 인증에 실패했습니다.");
    }

    @Test
    @DisplayName("회원가입_후_로그인_하지_않은_사용자의_최종_로그인_일시는_존재하지_않는다")
    void haveNoLastLoginDateForUserWhoHasNotLoggedInAfterSignup() {
        // given
        SignupCommand signupCommand = new SignupCommand(TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        userService.signup(signupCommand);

        // then
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID_1).getLastLoginDateTime()).isNull();
    }

    @Test
    @DisplayName("로그인을_하면_최종_로그인_일시가_변한다")
    void updateLastLoginDateWhenLogin() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        userService.updateLastLoginDateTime(TEST_GUID_1);

        // then
        assertThat(fakeUserRepository.wasCalled("updateLastLoginDateTime"))
                .isTrue();
        assertThat(fakeUserRepository.callCount("updateLastLoginDateTime"))
                .isEqualTo(1);
        assertThat(fakeUserRepository.lastLoginOf(TEST_GUID_1))
                .isEqualTo(fakeDateTimeProvider.now());
    }

    @Test
    @DisplayName("회원_정보를_조회했을_때_모든_정보가_조회된다")
    void fetchAllUserInformation() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        UserPosition userPosition = new UserPosition(user.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        fakeUserPositionRepository.saveAll(userPositions);

        UserSkill userSkill = new UserSkill(user.getUserGuid(), TEST_SKILL_CD);
        Set<UserSkill> userSkills = Set.of(userSkill);
        fakeUserSkillRepository.saveAll(userSkills);

        // when, then
        assertThat(userService.getCurrentUserProfile(user.getUserGuid())).isNotNull();
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getUsername()).isEqualTo(user.getUsername());
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getIntroduction()).isEqualTo(user.getIntroduction());
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getPositions()).isEqualTo(userPositions);
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getSkills()).isEqualTo(user.getSkills());
    }

    @Test
    @DisplayName("닉네임,자기소개는_값이_없거나_null_로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void nullOrEmptyUsernameAndIntroductionKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_GUID_1,null,null,null,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID_1).getUsername()).isEqualTo(user.getUsername());
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID_1).getIntroduction()).isEqualTo(user.getIntroduction());
    }

    @Test
    @DisplayName("닉네임,자기소개는_변경된_값으로_들어오면_해당_값으로_변경된다")
    void updateUsernameAndIntroductionCorrectly() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_GUID_1, NEW_USERNAME,NEW_INTRO,null,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID_1).getUsername()).isEqualTo(user.getUsername());
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID_1).getIntroduction()).isEqualTo(user.getIntroduction());
    }

    @Test
    @DisplayName("관심포지션이_값이_없거나_null_로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void nullOrEmptyUserPositionsKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        UserPosition userPosition = new UserPosition(user.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        fakeUserPositionRepository.saveAll(userPositions);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_GUID_1,null,null,null,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserPositionRepository.findByUserGuid(TEST_GUID_1)).isEqualTo(user.getPositions());
    }

    @Test
    @DisplayName("관심포지션이_새로운_값_으로_들어오면_새로운_값으로_변경된다")
    void updateNewUserPositionCorrectly() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        UserPosition userPosition = new UserPosition(user.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        fakeUserPositionRepository.saveAll(userPositions);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_GUID_1,null,null,NEW_POSITIONS,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserPositionRepository.findByUserGuid(TEST_GUID_1)).isEqualTo(user.getPositions());
    }

    @Test
    @DisplayName("관심포지션이_기존_값과_새로운_값_으로_들어오면_합쳐진_값으로_변경된다")
    void updateUserPositionWithExistAndNewCorrectly() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        UserPosition userPosition = new UserPosition(user.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        fakeUserPositionRepository.saveAll(userPositions);

        // when
        UserPosition userPosition1 = new UserPosition(user.getUserGuid(), TEST_POSITION_CD);
        UserPosition userPosition2 = new UserPosition(user.getUserGuid(), "002");
        Set<UserPosition> updateUserPosition = Set.of(userPosition1,userPosition2);
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_GUID_1,null,null, updateUserPosition,null);
        userService.updateProfile(updateProfileCommand);
        Set<UserPosition> positions = fakeUserPositionRepository.findByUserGuid(TEST_GUID_1);

        // then
        assertThat(positions)
                .hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder(TEST_POSITION_CD, "002");
    }

    @Test
    @DisplayName("회원탈퇴한_사용자의_deleted_값은_true_이다")
    void setDeletedTrueWhenUserWithdraws() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        RefreshToken refreshToken = new RefreshToken(TEST_EMAIL_1, "testToken");
        fakeRefreshTokenRepository.save(refreshToken);

        // when
        userService.withdrawUser(TEST_GUID_1);

        // then
        assertThat(fakeUserRepository.findByUserGuid(TEST_GUID_1).isDeleted()).isTrue();
        assertThat(fakeRefreshTokenRepository.findByUserGuid(TEST_GUID_1)).isNull();
    }

    @Test
    @DisplayName("일반_사용자는_USER_권한이_존재한다")
    void haveUserRoleForRegularUser() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when, then
        assertThat(userService.existsByUserRole(UserRole.USER)).isTrue();
        assertThat(userService.existsByUserRole(UserRole.ADMIN)).isFalse();
    }
}