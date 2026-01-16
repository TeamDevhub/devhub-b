package teamdevhub.devhub.small.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.fake.pure.provider.FakeDateTimeProvider;
import teamdevhub.devhub.fake.pure.provider.FakePasswordPolicyProvider;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.pure.repository.FakeUserPositionRepository;
import teamdevhub.devhub.fake.pure.repository.FakeUserRepository;
import teamdevhub.devhub.fake.pure.repository.FakeUserSkillRepository;
import teamdevhub.devhub.fake.pure.usecase.FakeSignupVerificationUseCase;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.service.user.UserService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserServiceTest {

    private UserService userService;
    private FakeUserRepository fakeUserRepository;
    private FakeUserPositionRepository fakeUserPositionRepository;
    private FakeUserSkillRepository fakeUserSkillRepository;
    private FakeSignupVerificationUseCase fakeSignupVerificationUseCase;
    private FakePasswordPolicyProvider fakePasswordPolicyProvider;
    private FakeDateTimeProvider fakeDateTimeProvider;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        fakeUserPositionRepository = new FakeUserPositionRepository();
        fakeUserSkillRepository = new FakeUserSkillRepository();

        fakeSignupVerificationUseCase = new FakeSignupVerificationUseCase();
        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_USER_GUID_1);
        fakePasswordPolicyProvider = new FakePasswordPolicyProvider();
        fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));

        userService = new UserService(
                fakeSignupVerificationUseCase,
                fakeUserRepository,
                fakeUserPositionRepository,
                fakeUserSkillRepository,
                fakePasswordPolicyProvider,
                fakeUuidIdentifierProvider,
                fakeDateTimeProvider
        );
    }

    @Test
    @DisplayName("관리자_계정을_생성한다")
    void createAdminAccount() {
        // given
        FakeUuidIdentifierProvider adminUuidProvider = new FakeUuidIdentifierProvider(ADMIN_USER_GUID);
        userService = new UserService(
                fakeSignupVerificationUseCase,
                fakeUserRepository,
                fakeUserPositionRepository,
                fakeUserSkillRepository,
                fakePasswordPolicyProvider,
                adminUuidProvider,
                fakeDateTimeProvider
        );

        // when
        userService.initializeAdminUser(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

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
        userService.getUserForLogin(TEST_EMAIL_1);

        // then
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(user.getEmail())).isNotNull();
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(user.getEmail()).userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(user.getEmail()).userRole()).isEqualTo(UserRole.USER);
    }

//    @Test
//    @DisplayName("회원가입에_성공하면_이메일_인증_테이블에_해당_사용자의_인증내역이_삭제된다")
//    void deleteEmailVerificationRecordWhenSuccessfulSignup() {
//        // given
//        SignupCommand signupCommand = new SignupCommand(TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, VERIFICATION_TARGET);
//
//        // when
//        User savedUser = userService.signup(signupCommand);
//
//        // then
//        assertThat(fakeSignupVerificationUseCase.getVerification(VERIFICATION_TARGET)).isNull();
//        assertThat(fakeUserRepository.save(savedUser).getUserGuid()).isEqualTo(TEST_USER_GUID_1);
//        assertThat(fakeUserRepository.save(savedUser).getPassword()).isEqualTo(fakePasswordPolicyProvider.encode(TEST_PASSWORD_1));
//    }
//
//    @Test
//    @DisplayName("이메일_인증이_완료되지_않은_사용자가_회원가입을_요청하면_예외를_던진다")
//    void throwExceptionWhenSignupWithoutEmailVerification() {
//        // given
//        SignupCommand signupCommand = new SignupCommand(UNVERIFIED_EMAIL, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);
//
//        // when
//        assertThatThrownBy(
//                () -> userService.signup(signupCommand))
//                // then
//                .isInstanceOf(BusinessRuleException.class)
//                .hasMessageContaining("E-mail 인증에 실패했습니다.");
//    }
//
//    @Test
//    @DisplayName("회원가입_후_로그인_하지_않은_사용자의_최종_로그인_일시는_존재하지_않는다")
//    void haveNoLastLoginDateForUserWhoHasNotLoggedInAfterSignup() {
//        // given
//        SignupCommand signupCommand = new SignupCommand(TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);
//
//        // when
//        userService.signup(signupCommand);
//
//        // then
//        assertThat(fakeUserRepository.wasCalled("updateLastLoginDateTime")).isFalse();
//    }

    @Test
    @DisplayName("로그인을_하면_최종_로그인_일시가_변한다")
    void updateLastLoginDateWhenLogin() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        userService.updateLastLoginDateTime(TEST_USER_GUID_1);

        // then
        assertThat(fakeUserRepository.wasCalled("updateLastLoginDateTime")).isTrue();
        assertThat(fakeUserRepository.callCount("updateLastLoginDateTime")).isEqualTo(1);
        assertThat(fakeUserRepository.lastLoginOf(user.getUserGuid())).isEqualTo(fakeDateTimeProvider.now());
    }

    @Test
    @DisplayName("사용자_정보를_조회했을_때_모든_정보가_조회된다")
    void fetchAllUserInformation() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        UserPosition userPosition = new UserPosition(user.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        fakeUserPositionRepository.saveAll(userPositions);

        UserSkill userSkill = new UserSkill(user.getUserGuid(), TEST_SKILL_CD);
        Set<UserSkill> userSkills = Set.of(userSkill);
        fakeUserSkillRepository.saveAll(userSkills);

        // when, then
        assertThat(userService.getCurrentUserProfile(user.getUserGuid())).isNotNull();
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getUsername()).isEqualTo(user.getUsername());
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getIntroduction()).isEqualTo(user.getIntroduction());
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getPositions()).isEqualTo(userPositions);
        assertThat(userService.getCurrentUserProfile(user.getUserGuid()).getSkills()).isEqualTo(user.getSkills());
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_닉네임,자기소개는_값이_없거나_null_로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void nullOrEmptyUsernameAndIntroductionKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,null,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserRepository.findByUserGuid(user.getUserGuid()).getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(fakeUserRepository.findByUserGuid(user.getUserGuid()).getIntroduction()).isEqualTo(TEST_INTRO_1);
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_닉네임,자기소개는_변경된_값으로_들어오면_해당_값으로_변경된다")
    void updateUsernameAndIntroductionCorrectly() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, NEW_USERNAME,NEW_INTRO,null,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserRepository.findByUserGuid(user.getUserGuid()).getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(fakeUserRepository.findByUserGuid(user.getUserGuid()).getIntroduction()).isEqualTo(NEW_INTRO);
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션이_값이_없거나_null_로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void nullOrEmptyUserPositionsKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        UserPosition userPosition = new UserPosition(user.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        fakeUserPositionRepository.saveAll(userPositions);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,null,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserPositionRepository.findByUserGuid(user.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(fakeUserPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션의_포지션코드_값이_null_로_들어오면_변경되지_않는다")
    void nullPositionCodeWithUserPositionsKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        Set<UserPosition> userPositions = new HashSet<>(Set.of(new UserPosition(user.getUserGuid(), null)));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null, userPositions,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserPositionRepository.findByUserGuid(user.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(fakeUserPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션의_포지션코드_값이_빈값으로_들어오면_변경되지_않는다")
    void emptyPositionCodeWithUserPositionsKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        Set<UserPosition> userPositions = new HashSet<>(Set.of(new UserPosition(user.getUserGuid(), "")));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,userPositions,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserPositionRepository.findByUserGuid(user.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(fakeUserPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션이_동일한_값으로_들어오면_변경되지_않는다")
    void sameUserPositionsKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,TEST_USER_POSITIONS,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserPositionRepository.findByUserGuid(user.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(fakeUserPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션이_새로운_값_으로_들어오면_새로운_값으로_변경된다")
    void updateNewUserPositionCorrectly() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null, NEW_USER_POSITIONS,null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserPositionRepository.findByUserGuid(user.getUserGuid())).isEqualTo(NEW_USER_POSITIONS);
        assertThat(fakeUserPositionRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션이_기존_값과_새로운_값_으로_들어오면_합쳐진_값으로_변경된다")
    void updateUserPositionWithExistAndNewCorrectly() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        UserPosition userPosition1 = new UserPosition(user.getUserGuid(), TEST_POSITION_CD);
        UserPosition userPosition2 = new UserPosition(user.getUserGuid(), NEW_POSITION_CD);
        Set<UserPosition> newUserPositions = Set.of(userPosition1,userPosition2);
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null, newUserPositions,null);
        userService.updateProfile(updateProfileCommand);
        Set<UserPosition> currentUserPositions = fakeUserPositionRepository.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(currentUserPositions)
                .hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder(TEST_POSITION_CD, NEW_POSITION_CD);
        assertThat(fakeUserPositionRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬이_값이_없거나_null_로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void nullOrEmptyUserSkillsKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserSkillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, null);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserSkillRepository.findByUserGuid(TEST_USER_GUID_1)).isEqualTo(TEST_USER_SKILLS);
        assertThat(fakeUserSkillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬의_스킬코드_값이_null_로_들어오면_변경되지_않는다")
    void nullSkillCodeWithUserSkillsKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserSkillRepository.saveAll(TEST_USER_SKILLS);

        // when
        Set<UserSkill> userSkills = new HashSet<>(Set.of(new UserSkill(user.getUserGuid(), null)));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, userSkills);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserSkillRepository.findByUserGuid(TEST_USER_GUID_1)).isEqualTo(TEST_USER_SKILLS);
        assertThat(fakeUserSkillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬의_스킬코드_값이_빈값으로_들어오면_변경되지_않는다")
    void emptySkillCodeWithUserSkillsKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserSkillRepository.saveAll(TEST_USER_SKILLS);

        // when
        Set<UserSkill> userSkills = new HashSet<>(Set.of(new UserSkill(user.getUserGuid(), "")));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, userSkills);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserSkillRepository.findByUserGuid(TEST_USER_GUID_1)).isEqualTo(TEST_USER_SKILLS);
        assertThat(fakeUserSkillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬이_동일한_값으로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void sameUserSkillsKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserSkillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, TEST_USER_SKILLS);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserSkillRepository.findByUserGuid(TEST_USER_GUID_1)).isEqualTo(TEST_USER_SKILLS);
        assertThat(fakeUserSkillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬이_새로운_값으로_들어오면_새로운_값으로_변경된다")
    void updateNewUserSkillCorrectly() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserSkillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, NEW_USER_SKILLS);
        userService.updateProfile(updateProfileCommand);

        // then
        assertThat(fakeUserSkillRepository.findByUserGuid(TEST_USER_GUID_1)).isEqualTo(NEW_USER_SKILLS);
        assertThat(fakeUserSkillRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심스킬이_기존_값과_새로운_값으로_들어오면_합쳐진_값으로_변경된다")
    void updateUserSkillWithExistAndNewCorrectly() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);
        fakeUserSkillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UserSkill userSkill1 = new UserSkill(user.getUserGuid(), TEST_SKILL_CD);
        UserSkill userSkill2 = new UserSkill(user.getUserGuid(), NEW_SKILL_CD);
        Set<UserSkill> newUserSkills = Set.of(userSkill1, userSkill2);

        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, newUserSkills);
        userService.updateProfile(updateProfileCommand);

        Set<UserSkill> currentUserSkills = fakeUserSkillRepository.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(currentUserSkills)
                .hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder(TEST_SKILL_CD, NEW_SKILL_CD);
        assertThat(fakeUserSkillRepository.replaceCalled).isTrue();
    }
//
//    @Test
//    @DisplayName("회원탈퇴한_사용자의_deleted_값은_true_이고_blocked_값은_false_이다")
//    void setDeletedTrueWhenUserWithdraws() {
//        // given
//        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
//        fakeUserRepository.save(user);
//        RefreshToken refreshToken = new RefreshToken(TEST_EMAIL_1, "testToken");
//        fakeRefreshTokenRepository.save(refreshToken);
//
//        // when
//        userService.withdrawUser(TEST_USER_GUID_1);
//
//        // then
//        assertThat(fakeUserRepository.findByUserGuid(TEST_USER_GUID_1).isDeleted()).isTrue();
//        assertThat(fakeUserRepository.findByUserGuid(TEST_USER_GUID_1).isBlocked()).isFalse();
//        assertThat(fakeRefreshTokenRepository.findByUserGuid(TEST_USER_GUID_1)).isNull();
//    }

    @Test
    @DisplayName("일반_사용자는_USER_권한이_존재한다")
    void haveUserRoleForGeneralUser() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when, then
        assertThat(userService.existsByUserRole(UserRole.USER)).isTrue();
        assertThat(userService.existsByUserRole(UserRole.ADMIN)).isFalse();
    }
}