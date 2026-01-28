package teamdevhub.devhub.small.core.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.user.OauthUser;
import teamdevhub.devhub.shared.exception.DomainRuleException;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.core.user.domain.vo.position.UserPositionChangeResult;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkillChangeResult;
import teamdevhub.devhub.core.user.domain.vo.user.CreateUserCommand;
import teamdevhub.devhub.core.user.domain.vo.user.UpdateUserCommand;
import teamdevhub.devhub.core.auth.port.in.command.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserTest {

    @Test
    @DisplayName("관리자_권한의_사용자를_생성한다")
    void createAdminUser() {
        // given
        SignupAdminCommand signupAdminCommand = SignupAdminCommand.builder()
                .userGuid(null)
                .email(ADMIN_EMAIL_1)
                .password(ADMIN_PASSWORD_1)
                .username(ADMIN_USERNAME_1)
                .introduction("")
                .positionList(List.of())
                .skillList(List.of())
                .verificationTarget(null)
                .build();
        CreateUserCommand adminCreateUserCommand = CreateUserCommand.adminUserCreateCommand(signupAdminCommand, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);

        // when
        User createdAdminUser = User.createAdminUser(adminCreateUserCommand);

        // then
        assertThat(createdAdminUser.getUserRole()).isEqualTo(UserRole.ADMIN);
        assertThat(createdAdminUser.getVerificationProvider()).isEqualTo(VerificationProvider.EMAIL);
        assertThat(createdAdminUser.getUsername()).isEqualTo(ADMIN_USERNAME_1);
        assertThat(createdAdminUser.getPositions()).isEmpty();
        assertThat(createdAdminUser.getSkills()).isEmpty();
        assertThat(createdAdminUser.isDeleted()).isFalse();
        assertThat(createdAdminUser.isBlocked()).isFalse();
    }

    @Test
    @DisplayName("일반_권한의_사용자를_생성한다")
    void createGeneralUser() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);

        // when
        User createdGeneralUser = User.createGeneralUser(generalCreateUserCommand);

        // then
        assertThat(createdGeneralUser.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(createdGeneralUser.getVerificationProvider()).isEqualTo(VerificationProvider.EMAIL);
        assertThat(createdGeneralUser.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(createdGeneralUser.getPassword()).isEqualTo(TEST_PASSWORD_1);
        assertThat(createdGeneralUser.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(createdGeneralUser.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(createdGeneralUser.getIntroduction()).isEqualTo(TEST_INTRO_1);
        assertThat(createdGeneralUser.isDeleted()).isFalse();
        assertThat(createdGeneralUser.isBlocked()).isFalse();
        assertThat(createdGeneralUser.getMannerDegree()).isEqualTo(36.5);
    }

    @Test
    @DisplayName("Oauth_사용자를_생성한다")
    void createOauthUser() {
        // given
        OauthUser oauthUser = new OauthUser("testOauthId", VerificationProvider.GOOGLE, TEST_EMAIL_1);
        SignupOauthUserCommand signupOauthUserCommand = SignupOauthUserCommand.builder()
                .tempToken("lastOauthUser")
                .username(TEST_USERNAME_1)
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();
        CreateUserCommand oauthCreateUserCommand = CreateUserCommand.oauthUserCreateCommand(signupOauthUserCommand, oauthUser, TEST_USER_GUID_1, TEST_PASSWORD_1);

        // when
        User createdOauthUser = User.createOauthUser(oauthCreateUserCommand);

        // then
        assertThat(createdOauthUser.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(createdOauthUser.getVerificationProvider()).isEqualTo(oauthUser.verificationProvider());
        assertThat(createdOauthUser.getEmail()).isEqualTo(oauthUser.email());
        assertThat(createdOauthUser.getPassword()).isEqualTo(signupOauthUserCommand.password());
        assertThat(createdOauthUser.getUsername()).isEqualTo(signupOauthUserCommand.username());
        assertThat(createdOauthUser.isDeleted()).isFalse();
        assertThat(createdOauthUser.isBlocked()).isFalse();
        assertThat(createdOauthUser.getMannerDegree()).isEqualTo(36.5);
    }

    @Test
    @DisplayName("사용자의_이메일_값이_공백이면_예외를_던진다")
    void throwIfEmailIsBlank() {
        //given
        CreateUserCommand createUserCommand = new CreateUserCommand(TEST_USER_GUID_1, VerificationProvider.EMAIL, TEST_EMAIL_1,"", TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);

        assertThatThrownBy(
                // given,when
                () -> User.createGeneralUser(createUserCommand)
        )
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("사용자 ID 값이 잘못되었습니다");
    }

    @Test
    @DisplayName("탈퇴한_사용자는_deleted_값이_true_다")
    void isDeletedUser() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        // when
        testUser.withdraw();

        // then
        assertThat(testUser.isDeleted()).isTrue();
        assertThat(testUser.isBlocked()).isFalse();
    }

    @Test
    @DisplayName("이미_탈퇴한_회원이_재탈퇴를_요청하면_예외를_던진다")
    void throwIfAlreadyDeleted() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);
        testUser.withdraw();

        // when
        assertThatThrownBy(testUser::withdraw)
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("이미 탈퇴한 회원입니다");
    }

    @Test
    @DisplayName("사용자_프로필_정보를_새로운값으로_변경하면_기존값이_변경된다")
    void updateUserProfile() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        // when
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(NEW_USERNAME, NEW_INTRO);
        testUser.updateBasicProfile(updateUserCommand);

        // then
        assertThat(testUser.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(testUser.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(testUser.getIntroduction()).isEqualTo(NEW_INTRO);
    }

    @Test
    @DisplayName("변경을_요청한_값이_빈값_또는_이전과_같은_값이라면_기존값은_변경되지_않는다")
    void keepUserProfileIfNotBlankOrChanged() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        // when
        UpdateUserCommand updateUserCommand = new UpdateUserCommand("", "");
        testUser.updateBasicProfile(updateUserCommand);

        // then
        assertThat(testUser.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(testUser.getIntroduction()).isEqualTo(TEST_INTRO_1);
    }

    @Test
    @DisplayName("관심포지션이_변경되면_UserPositionChangeResult_의_changed_는_true_이다")
    void changedIsTrueWhenPositionsChange() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        UserPosition oldPosition = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        testUser.loadPositionsAndSkills(new HashSet<>(Set.of(oldPosition)), new HashSet<>());

        // when
        UserPosition newPosition1 = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        UserPosition newPosition2 = new UserPosition(TEST_USER_GUID_1, NEW_POSITION_CD);
        Set<UserPosition> changedPositions = new HashSet<>(Set.of(newPosition1, newPosition2));
        UserPositionChangeResult userPositionChangeResult = testUser.changePositions(changedPositions);

        // then
        assertThat(userPositionChangeResult.changed()).isTrue();
        assertThat(userPositionChangeResult.previousPositions()).containsExactlyInAnyOrder(oldPosition);
        assertThat(userPositionChangeResult.changedPositions()).containsExactlyInAnyOrder(newPosition1, newPosition2);
    }

    @Test
    @DisplayName("관심포지션의_포지션코드가_null_로_들어오면_UserPositionChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenPositionsCodeIsNull() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        UserPosition previousPosition = new UserPosition(TEST_USER_GUID_1, "001");
        testUser.loadPositionsAndSkills(Set.of(previousPosition), Set.of());

        // when
        UserPosition newPosition = new UserPosition(TEST_USER_GUID_1, null);
        Set<UserPosition> newPositions = new HashSet<>();
        newPositions.add(newPosition);
        UserPositionChangeResult userPositionChangeResult = testUser.changePositions(newPositions);

        // then
        assertThat(userPositionChangeResult.changed()).isFalse();
        assertThat(userPositionChangeResult.previousPositions()).containsExactly(previousPosition);
        assertThat(userPositionChangeResult.changedPositions()).containsExactly(previousPosition);
    }

    @Test
    @DisplayName("관심포지션의_포지션코드가_동일한_값으로_들어오면_UserPositionChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenPositionsCodeIsSameValue() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        UserPosition previousPosition = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        testUser.loadPositionsAndSkills(Set.of(previousPosition), Set.of());

        // when
        UserPosition newPosition = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        Set<UserPosition> newPositions = new HashSet<>();
        newPositions.add(newPosition);
        UserPositionChangeResult userPositionChangeResult = testUser.changePositions(newPositions);

        // then
        assertThat(userPositionChangeResult.changed()).isFalse();
        assertThat(userPositionChangeResult.previousPositions()).containsExactly(previousPosition);
        assertThat(userPositionChangeResult.changedPositions()).containsExactly(newPosition);
    }

    @Test
    @DisplayName("관심포지션의_값_자체가_null_또는_빈값이면_UserPositionChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenUserPositionsIsNullOrEmpty() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        // when
        UserPositionChangeResult nullResult = testUser.changePositions(null);
        UserPositionChangeResult emptyResult = testUser.changePositions(Set.of());

        // then
        assertThat(nullResult.changed()).isFalse();
        assertThat(emptyResult.changed()).isFalse();
    }

    @Test
    @DisplayName("보유스킬이_변경되면_UserSkillChangeResult_의_changed_는_true_이다")
    void changedIsTrueWhenSkillsChange() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        UserSkill oldSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        testUser.loadPositionsAndSkills(Set.of(), new HashSet<>(Set.of(oldSkill)));

        // when
        UserSkill newSkill1 = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        UserSkill newSkill2 = new UserSkill(TEST_USER_GUID_1, NEW_SKILL_CD);
        Set<UserSkill> changedSkills = new HashSet<>(Set.of(newSkill1, newSkill2));

        UserSkillChangeResult result = testUser.changeSkills(changedSkills);

        // then
        assertThat(result.changed()).isTrue();
        assertThat(result.previousSkills()).containsExactly(oldSkill);
        assertThat(result.changedSkills()).containsExactlyInAnyOrder(newSkill1, newSkill2);
    }


    @Test
    @DisplayName("보유스킬의_스킬코드가_null_로_들어오면_UserSkillChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenSkillsCodeIsNull() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        UserSkill previousSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        testUser.loadPositionsAndSkills(Set.of(), Set.of(previousSkill));

        // when
        UserSkill newSkill = new UserSkill(TEST_USER_GUID_1, null);
        Set<UserSkill> newSkills = new HashSet<>();
        newSkills.add(newSkill);

        UserSkillChangeResult result = testUser.changeSkills(newSkills);

        // then
        assertThat(result.changed()).isFalse();
        assertThat(result.previousSkills()).containsExactly(previousSkill);
        assertThat(result.changedSkills()).containsExactly(previousSkill);
    }

    @Test
    @DisplayName("보유스킬의_스킬코드가_동일한_값으로_들어오면_UserSkillChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenSkillsCodeIsSameValue() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        UserSkill previousSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        testUser.loadPositionsAndSkills(Set.of(), Set.of(previousSkill));

        // when
        UserSkill newSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        Set<UserSkill> newSkills = new HashSet<>();
        newSkills.add(newSkill);

        UserSkillChangeResult result = testUser.changeSkills(newSkills);

        // then
        assertThat(result.changed()).isFalse();
        assertThat(result.previousSkills()).containsExactly(previousSkill);
        assertThat(result.changedSkills()).containsExactly(newSkill);
    }

    @Test
    @DisplayName("보유스킬의_값_자체가_null_또는_빈값이면_UserSkillChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenUserSkillsIsNullOrEmpty() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        // when
        UserSkillChangeResult nullResult = testUser.changeSkills(null);
        UserSkillChangeResult emptyResult = testUser.changeSkills(Set.of());

        // then
        assertThat(nullResult.changed()).isFalse();
        assertThat(emptyResult.changed()).isFalse();
    }

    @Test
    @DisplayName("사용자의_관심포지션과_보유스킬을_초기화한다")
    void loadPositionsAndSkills_initializesPositionsAndSkills() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        UserPosition userPosition = new UserPosition(TEST_USER_GUID_1, "001");
        UserSkill userSkill = new UserSkill(TEST_USER_GUID_1, "SKILL_001");

        // when
        testUser.loadPositionsAndSkills(Set.of(userPosition), Set.of(userSkill));

        // then
        assertThat(testUser.getPositions()).containsExactly(userPosition);
        assertThat(testUser.getSkills()).containsExactly(userSkill);
    }
}