package teamdevhub.devhub.small.application.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.user.UserProfileService;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserPositionRepository;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserSkillRepository;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserProfileServiceTest {

    private UserProfileService userProfileService;
    private FakeUserRepository fakeUserRepository;
    private FakeUserPositionRepository fakeUserPositionRepository;
    private FakeUserSkillRepository fakeUserSkillRepository;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        fakeUserPositionRepository = new FakeUserPositionRepository();
        fakeUserSkillRepository = new FakeUserSkillRepository();

        userProfileService = new UserProfileService(
                fakeUserRepository,
                fakeUserPositionRepository,
                fakeUserSkillRepository
        );
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
        assertThat(userProfileService.getCurrentUserProfile(user.getUserGuid())).isNotNull();
        assertThat(userProfileService.getCurrentUserProfile(user.getUserGuid()).getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userProfileService.getCurrentUserProfile(user.getUserGuid()).getUsername()).isEqualTo(user.getUsername());
        assertThat(userProfileService.getCurrentUserProfile(user.getUserGuid()).getIntroduction()).isEqualTo(user.getIntroduction());
        assertThat(userProfileService.getCurrentUserProfile(user.getUserGuid()).getPositions()).isEqualTo(userPositions);
        assertThat(userProfileService.getCurrentUserProfile(user.getUserGuid()).getSkills()).isEqualTo(user.getSkills());
    }

    @Test
    @DisplayName("사용자_프로필_수정에서_닉네임,자기소개는_값이_없거나_null_로_들어오면_사용자가_기본적으로_가지고_있는_값으로_유지된다")
    void nullOrEmptyUsernameAndIntroductionKeepsExistingValues() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        UpdateProfileCommand updateProfileCommand = new UpdateProfileCommand(TEST_USER_GUID_1,null,null,null,null);
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);
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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

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
        userProfileService.updateProfile(updateProfileCommand);

        Set<UserSkill> currentUserSkills = fakeUserSkillRepository.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(currentUserSkills)
                .hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder(TEST_SKILL_CD, NEW_SKILL_CD);
        assertThat(fakeUserSkillRepository.replaceCalled).isTrue();
    }

    @Test
    @DisplayName("일반_사용자는_USER_권한이_존재한다")
    void haveUserRoleForGeneralUser() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when, then
        assertThat(userProfileService.getCurrentUserProfile(user.getUserGuid()).getUserRole()).isEqualTo(UserRole.USER);
    }
}