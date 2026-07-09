package teamdevhub.devhub.small.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.core.user.application.service.UserProfileService;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileImageCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserPositionRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserSkillRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeEncodedPasswordProvider;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserProfileServiceTest {

    private UserProfileService userProfileService;

    private FakeEncodedPasswordProvider encodedPasswordProvider;
    private FakeUserRepository userRepository;
    private FakeUserPositionRepository userPositionRepository;
    private FakeUserSkillRepository skillRepository;

    @BeforeEach
    void init() {

        userRepository = new FakeUserRepository();
        userPositionRepository = new FakeUserPositionRepository();
        skillRepository = new FakeUserSkillRepository();

        userProfileService = new UserProfileService(
                userRepository,
                userPositionRepository,
                skillRepository
        );
    }

    @Test
    @DisplayName("사용자_정보를_조회했을_때_모든_정보가_조회된다")
    void fetchAllUserInformation() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);
        userRepository.save(testUser);

        UserPosition userPosition = new UserPosition(testUser.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        userPositionRepository.saveAll(userPositions);

        UserSkill userSkill = new UserSkill(testUser.getUserGuid(), TEST_SKILL_CD);
        Set<UserSkill> userSkills = Set.of(userSkill);
        skillRepository.saveAll(userSkills);

        // when, then
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid())).isNotNull();
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getUserGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getUsername()).isEqualTo(testUser.getUsername());
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getIntroduction()).isEqualTo(testUser.getIntroduction());
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getPositions()).isEqualTo(userPositions);
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getSkills()).isEqualTo(testUser.getSkills());
    }

    @Test
    @DisplayName("사용자_프로필_이미지를_수정하면_해당_이미지로_변경된다")
    void updateProfileImageCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        CreateUserCommand generalCreateUserCommand =
                CreateUserCommand.generalUserCreateCommand(
                        signupUserCommand,
                        TEST_USER_GUID_1
                );

        User testUser = User.createGeneralUser(generalCreateUserCommand);
        userRepository.save(testUser);

        String NEW_PROFILE_IMAGE_GUID = "NEW_PROFILE_IMAGE_GUID";

        UpdateProfileImageCommand updateProfileImageCommand = new UpdateProfileImageCommand(TEST_USER_GUID_1, NEW_PROFILE_IMAGE_GUID);

        // when
        userProfileService.updateProfileImage(updateProfileImageCommand);

        // then
        User updatedUser = userRepository.findByUserGuid(TEST_USER_GUID_1);
        assertThat(updatedUser.getFileGuid()).isEqualTo(NEW_PROFILE_IMAGE_GUID);
    }

    @Test
    @DisplayName("기존_프로필_이미지가_있을_때_새로운_이미지로_덮어쓴다")
    void overwriteProfileImage() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        CreateUserCommand generalCreateUserCommand =
                CreateUserCommand.generalUserCreateCommand(
                        signupUserCommand,
                        TEST_USER_GUID_1
                );

        User testUser = User.createGeneralUser(generalCreateUserCommand);
        testUser.updateProfileImage(new UpdateProfileImageCommand(TEST_USER_GUID_1, "OLD_IMAGE_GUID"));

        userRepository.save(testUser);
        UpdateProfileImageCommand updateProfileImageCommand = new UpdateProfileImageCommand(TEST_USER_GUID_1, "NEW_IMAGE_GUID");

        // when
        userProfileService.updateProfileImage(updateProfileImageCommand);

        // then
        User updatedUser = userRepository.findByUserGuid(TEST_USER_GUID_1);
        assertThat(updatedUser.getFileGuid()).isEqualTo("NEW_IMAGE_GUID");
    }

    @Test
    @DisplayName("존재하지_않는_사용자의_프로필_이미지를_수정하면_예외가_발생한다")
    void updateProfileImageWithInvalidUser() {
        UpdateProfileImageCommand updateProfileImageCommand = new UpdateProfileImageCommand("NOT_EXIST_GUID", "IMAGE_GUID");

        assertThatThrownBy(() ->
                userProfileService.updateProfileImage(updateProfileImageCommand)
        ).isInstanceOf(AdapterDataException.class);
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_닉네임,자기소개는_값이_없거나_null_로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void nullOrEmptyUsernameAndIntroductionKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);
        userRepository.save(testUser);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,null,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).getUsername()).isEqualTo(testUser.getUsername());
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).getIntroduction()).isEqualTo(testUser.getIntroduction());
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_닉네임,자기소개는_변경된_값으로_들어오면_해당_값으로_변경된다")
    void updateUsernameAndIntroductionCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);
        userRepository.save(testUser);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, NEW_USERNAME,NEW_INTRO,null,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).getUsername()).isEqualTo(testUser.getUsername());
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).getIntroduction()).isEqualTo(testUser.getIntroduction());
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션이_값이_없거나_null_로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void nullOrEmptyUserPositionsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);

        UserPosition userPosition = new UserPosition(testUser.getUserGuid(), TEST_POSITION_CD);
        Set<UserPosition> userPositions = Set.of(userPosition);
        userPositionRepository.saveAll(userPositions);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,null,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션의_포지션코드_값이_null_로_들어오면_변경되지_않는다")
    void nullPositionCodeWithUserPositionsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        Set<UserPosition> userPositions = new HashSet<>(Set.of(new UserPosition(testUser.getUserGuid(), null)));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null, userPositions,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션의_포지션코드_값이_빈값으로_들어오면_변경되지_않는다")
    void emptyPositionCodeWithUserPositionsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        Set<UserPosition> userPositions = new HashSet<>(Set.of(new UserPosition(testUser.getUserGuid(), "")));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,userPositions,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션이_동일한_값으로_들어오면_변경되지_않는다")
    void sameUserPositionsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,TEST_USER_POSITIONS,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션이_새로운_값_으로_들어오면_새로운_값으로_변경된다")
    void updateNewUserPositionCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null, NEW_USER_POSITIONS,null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(userPositionRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(NEW_USER_POSITIONS);
        assertThat(userPositionRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심포지션이_기존_값과_새로운_값_으로_들어오면_합쳐진_값으로_변경된다")
    void updateUserPositionWithExistAndNewCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        userPositionRepository.saveAll(TEST_USER_POSITIONS);

        // when
        UserPosition userPosition1 = new UserPosition(testUser.getUserGuid(), TEST_POSITION_CD);
        UserPosition userPosition2 = new UserPosition(testUser.getUserGuid(), NEW_POSITION_CD);
        Set<UserPosition> newUserPositions = Set.of(userPosition1,userPosition2);
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null, newUserPositions,null);
        userProfileService.updateProfile(updateProfileCommand);
        Set<UserPosition> currentUserPositions = userPositionRepository.findByUserGuid(testUser.getUserGuid());

        // then
        assertThat(currentUserPositions)
                .hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder(TEST_POSITION_CD, NEW_POSITION_CD);
        assertThat(userPositionRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬이_값이_없거나_null_로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void nullOrEmptyUserSkillsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, null);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬의_스킬코드_값이_null_로_들어오면_변경되지_않는다")
    void nullSkillCodeWithUserSkillsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        Set<UserSkill> userSkills = new HashSet<>(Set.of(new UserSkill(testUser.getUserGuid(), null)));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, userSkills);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬의_스킬코드_값이_빈값으로_들어오면_변경되지_않는다")
    void emptySkillCodeWithUserSkillsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        Set<UserSkill> userSkills = new HashSet<>(Set.of(new UserSkill(testUser.getUserGuid(), "")));
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, userSkills);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬이_동일한_값으로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void sameUserSkillsKeepsExistingValues() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, TEST_USER_SKILLS);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(TEST_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isFalse();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_보유스킬이_새로운_값으로_들어오면_새로운_값으로_변경된다")
    void updateNewUserSkillCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, NEW_USER_SKILLS);
        userProfileService.updateProfile(updateProfileCommand);

        // then
        assertThat(skillRepository.findByUserGuid(testUser.getUserGuid())).isEqualTo(NEW_USER_SKILLS);
        assertThat(skillRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_관심스킬이_기존_값과_새로운_값으로_들어오면_합쳐진_값으로_변경된다")
    void updateUserSkillWithExistAndNewCorrectly() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        skillRepository.saveAll(TEST_USER_SKILLS);

        // when
        UserSkill userSkill1 = new UserSkill(testUser.getUserGuid(), TEST_SKILL_CD);
        UserSkill userSkill2 = new UserSkill(testUser.getUserGuid(), NEW_SKILL_CD);
        Set<UserSkill> newUserSkills = Set.of(userSkill1, userSkill2);

        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1, null, null, null, newUserSkills);
        userProfileService.updateProfile(updateProfileCommand);

        Set<UserSkill> currentUserSkills = skillRepository.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(currentUserSkills)
                .hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder(TEST_SKILL_CD, NEW_SKILL_CD);
        assertThat(skillRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("리뷰_점수로_매너도를_업데이트하면_저장소에_반영된다")
    void updateUserMannerDegree_validScore_delegatesToRepository() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);
        double reviewScore = 1.5;

        // when
        userProfileService.updateUserMannerDegree(TEST_USER_GUID_1, reviewScore);

        // then
        assertThat(userRepository.wasCalled("findByUserGuid")).isTrue();
        assertThat(userRepository.wasCalled("save")).isTrue();
        assertThat(userRepository.findByUserGuid(TEST_USER_GUID_1).getMannerDegree()).isEqualTo(36.5 + (reviewScore - 3));
    }

    @Test
    @DisplayName("일반_사용자는_USER_권한이_존재한다")
    void haveUserRoleForGeneralUser() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);

        // when, then
        assertThat(userProfileService.getCurrentUserProfile(testUser.getUserGuid()).getUserRole()).isEqualTo(UserRole.USER);
    }
}