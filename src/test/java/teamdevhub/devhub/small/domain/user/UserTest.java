package teamdevhub.devhub.small.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserPositionChangeResult;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.domain.user.vo.UserSkillChangeResult;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserTest {

    @Test
    @DisplayName("관리자_권한의_사용자를_생성한다")
    void isAdminAccountPositionsAndSkillsEmpty() {
        // given, when
        User adminUser = User.createAdminUser(ADMIN_USER_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        // then
        assertThat(adminUser.getUserRole()).isEqualTo(UserRole.ADMIN);
        assertThat(adminUser.getUsername()).isEqualTo(ADMIN_USERNAME);
        assertThat(adminUser.getPositions()).isEmpty();
        assertThat(adminUser.getSkills()).isEmpty();
        assertThat(adminUser.isDeleted()).isFalse();
        assertThat(adminUser.isBlocked()).isFalse();
    }

    // need to change
    @Test
    @DisplayName("일반_권한의_사용자를_생성한다")
    void createUserWithPositionsAndSkills() {
        // given,when
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // then
        assertThat(user.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(user.getPassword()).isEqualTo(TEST_PASSWORD_1);
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO_1);
        assertThat(user.isDeleted()).isFalse();
        assertThat(user.isBlocked()).isFalse();
        assertThat(user.getMannerDegree()).isEqualTo(36.5);
    }

    @Test
    @DisplayName("사용자의_이메일_값이_공백이면_예외를_던진다")
    void throwIfEmailIsBlank() {

        assertThatThrownBy(
                // given,when
                () -> User.createGeneralUser(TEST_USER_GUID_1, "", TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1)
        )
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("사용자 ID 값이 잘못되었습니다.");
    }

    @Test
    @DisplayName("사용자의_비밀번호_길이가_8글자보다_짧으면_예외를_던진다")
    void throwIfPasswordTooShort() {

        assertThatThrownBy(
                // given,when
                () -> User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, "123456", TEST_USERNAME_1, TEST_INTRO_1)
        )
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("사용자 비밀번호 값이 잘못되었습니다.");
    }

    @Test
    @DisplayName("탈퇴한_사용자는_deleted_값이_true_다")
    void isDeletedUser() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        user.withdraw();

        // then
        assertThat(user.isDeleted()).isTrue();
        assertThat(user.isBlocked()).isFalse();
    }

    @Test
    @DisplayName("이미_탈퇴한_회원이_재탈퇴를_요청하면_예외를_던진다")
    void throwIfAlreadyDeleted() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        user.withdraw();

        // when
        assertThatThrownBy(user::withdraw)
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("이미 탈퇴한 회원입니다.");
    }

    @Test
    @DisplayName("사용자_프로필_정보를_새로운값으로_변경하면_기존값이_변경된다")
    void updateUserProfile() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        user.updateUsernameAndIntroduction(NEW_USERNAME, NEW_INTRO);

        // then
        assertThat(user.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(user.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(user.getIntroduction()).isEqualTo(NEW_INTRO);
    }

    @Test
    @DisplayName("변경을_요청한_값이_빈값_또는_이전과_같은_값이라면_기존값은_변경되지_않는다")
    void keepUserProfileIfNotBlankOrChanged() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        user.updateUsernameAndIntroduction("", null);

        // then
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO_1);
    }

    @Test
    @DisplayName("관심포지션이_변경되면_UserPositionChangeResult_의_changed_는_true_이다")
    void changedIsTrueWhenPositionsChange() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        UserPosition oldPosition = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        user.loadPositionsAndSkills(new HashSet<>(Set.of(oldPosition)), new HashSet<>());

        // when
        UserPosition newPosition1 = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        UserPosition newPosition2 = new UserPosition(TEST_USER_GUID_1, NEW_POSITION_CD);
        Set<UserPosition> changedPositions = new HashSet<>(Set.of(newPosition1, newPosition2));
        UserPositionChangeResult userPositionChangeResult = user.changePositions(changedPositions);

        // then
        assertThat(userPositionChangeResult.changed()).isTrue();
        assertThat(userPositionChangeResult.previousPositions()).containsExactlyInAnyOrder(oldPosition);
        assertThat(userPositionChangeResult.changedPositions()).containsExactlyInAnyOrder(newPosition1, newPosition2);
    }

    @Test
    @DisplayName("관심포지션의_포지션코드가_null_로_들어오면_UserPositionChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenPositionsCodeIsNull() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        UserPosition previousPosition = new UserPosition(TEST_USER_GUID_1, "001");
        user.loadPositionsAndSkills(Set.of(previousPosition), Set.of());

        // when
        UserPosition newPosition = new UserPosition(TEST_USER_GUID_1, null);
        Set<UserPosition> newPositions = new HashSet<>();
        newPositions.add(newPosition);
        UserPositionChangeResult userPositionChangeResult = user.changePositions(newPositions);

        // then
        assertThat(userPositionChangeResult.changed()).isFalse();
        assertThat(userPositionChangeResult.previousPositions()).containsExactly(previousPosition);
        assertThat(userPositionChangeResult.changedPositions()).containsExactly(previousPosition);
    }

    @Test
    @DisplayName("관심포지션의_포지션코드가_동일한_값으로_들어오면_UserPositionChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenPositionsCodeIsSameValue() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        UserPosition previousPosition = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        user.loadPositionsAndSkills(Set.of(previousPosition), Set.of());

        // when
        UserPosition newPosition = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        Set<UserPosition> newPositions = new HashSet<>();
        newPositions.add(newPosition);
        UserPositionChangeResult userPositionChangeResult = user.changePositions(newPositions);

        // then
        assertThat(userPositionChangeResult.changed()).isFalse();
        assertThat(userPositionChangeResult.previousPositions()).containsExactly(previousPosition);
        assertThat(userPositionChangeResult.changedPositions()).containsExactly(newPosition);
    }

    @Test
    @DisplayName("관심포지션의_값_자체가_null_또는_빈값이면_UserPositionChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenUserPositionsIsNullOrEmpty() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        UserPositionChangeResult nullResult = user.changePositions(null);
        UserPositionChangeResult emptyResult = user.changePositions(Set.of());

        // then
        assertThat(nullResult.changed()).isFalse();
        assertThat(emptyResult.changed()).isFalse();
    }

    @Test
    @DisplayName("보유스킬이_변경되면_UserSkillChangeResult_의_changed_는_true_이다")
    void changedIsTrueWhenSkillsChange() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        UserSkill oldSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        user.loadPositionsAndSkills(Set.of(), new HashSet<>(Set.of(oldSkill)));

        // when
        UserSkill newSkill1 = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        UserSkill newSkill2 = new UserSkill(TEST_USER_GUID_1, NEW_SKILL_CD);
        Set<UserSkill> changedSkills = new HashSet<>(Set.of(newSkill1, newSkill2));

        UserSkillChangeResult result = user.changeSkills(changedSkills);

        // then
        assertThat(result.changed()).isTrue();
        assertThat(result.previousSkills()).containsExactly(oldSkill);
        assertThat(result.changedSkills()).containsExactlyInAnyOrder(newSkill1, newSkill2);
    }


    @Test
    @DisplayName("보유스킬의_스킬코드가_null_로_들어오면_UserSkillChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenSkillsCodeIsNull() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        UserSkill previousSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        user.loadPositionsAndSkills(Set.of(), Set.of(previousSkill));

        // when
        UserSkill newSkill = new UserSkill(TEST_USER_GUID_1, null);
        Set<UserSkill> newSkills = new HashSet<>();
        newSkills.add(newSkill);

        UserSkillChangeResult result = user.changeSkills(newSkills);

        // then
        assertThat(result.changed()).isFalse();
        assertThat(result.previousSkills()).containsExactly(previousSkill);
        assertThat(result.changedSkills()).containsExactly(previousSkill);
    }

    @Test
    @DisplayName("보유스킬의_스킬코드가_동일한_값으로_들어오면_UserSkillChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenSkillsCodeIsSameValue() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        UserSkill previousSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        user.loadPositionsAndSkills(Set.of(), Set.of(previousSkill));

        // when
        UserSkill newSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        Set<UserSkill> newSkills = new HashSet<>();
        newSkills.add(newSkill);

        UserSkillChangeResult result = user.changeSkills(newSkills);

        // then
        assertThat(result.changed()).isFalse();
        assertThat(result.previousSkills()).containsExactly(previousSkill);
        assertThat(result.changedSkills()).containsExactly(newSkill);
    }

    @Test
    @DisplayName("보유스킬의_값_자체가_null_또는_빈값이면_UserSkillChangeResult_의_changed_는_false_이다")
    void changedIsFalseWhenUserSkillsIsNullOrEmpty() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        UserSkillChangeResult nullResult = user.changeSkills(null);
        UserSkillChangeResult emptyResult = user.changeSkills(Set.of());

        // then
        assertThat(nullResult.changed()).isFalse();
        assertThat(emptyResult.changed()).isFalse();
    }

    @Test
    @DisplayName("사용자의_관심포지션과_보유스킬을_초기화한다")
    void loadPositionsAndSkills_initializesPositionsAndSkills() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        UserPosition userPosition = new UserPosition(TEST_USER_GUID_1, "001");
        UserSkill userSkill = new UserSkill(TEST_USER_GUID_1, "SKILL_001");

        // when
        user.loadPositionsAndSkills(Set.of(userPosition), Set.of(userSkill));

        // then
        assertThat(user.getPositions()).containsExactly(userPosition);
        assertThat(user.getSkills()).containsExactly(userSkill);
    }
}