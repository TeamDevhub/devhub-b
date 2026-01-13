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
import static teamdevhub.devhub.constant.TestConstant.*;

class UserTest {

    @Test
    @DisplayName("관리자_계정은_관심_포지션과_보유_스킬이_빈_값이다")
    void isAdminAccountPositionsAndSkillsEmpty() {
        // given, when
        User adminUser = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

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
    @DisplayName("관심_포지션과_보유_스킬을_리스트로_받고_일반_유저를_생성한다")
    void createUserWithPositionsAndSkills() {
        // given,when
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // then
        assertThat(user.getUserGuid()).isEqualTo(TEST_GUID_1);
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
    @DisplayName("이메일_값이_공백이면_예외를_던진다")
    void throwIfEmailIsBlank() {

        assertThatThrownBy(
                // given,when
                () -> User.createGeneralUser(TEST_GUID_1, "", TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1)
        )
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("사용자 ID 값이 잘못되었습니다.");
    }

    @Test
    @DisplayName("비밀번호_길이가_8자보다_짧으면_예외를_던진다")
    void throwIfPasswordTooShort() {

        assertThatThrownBy(
                // given,when
                () -> User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, "123456", TEST_USERNAME_1, TEST_INTRO_1)
        )
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("사용자 비밀번호 값이 잘못되었습니다.");
    }

    @Test
    @DisplayName("관리자_계정은_보유_스킬이_빈_값이어도_예외를_던지지_않는다")
    void allowAdminWithEmptySkills() {
        // given, when
        User adminUser = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        // then
        assertThat(adminUser.getUserRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("탈퇴한_회원은_deleted_값이_true_다")
    void isDeletedUser() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

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
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        user.withdraw();

        // when
        assertThatThrownBy(user::withdraw)
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("이미 탈퇴한 회원입니다.");
    }

    @Test
    @DisplayName("사용자_프로필_정보를_새로운_값으로_변경하면_기존_값이_변경된다")
    void updateUserProfile() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        user.updateUsernameAndIntroduction(NEW_USERNAME, NEW_INTRO);

        // then
        assertThat(user.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(user.getIntroduction()).isEqualTo(NEW_INTRO);
    }

    @Test
    @DisplayName("빈_값_또는_이전과_같은_값으로_변경하면_기존_값은_변경되지_않는다")
    void updateUserProfileIfNotBlankOrChanged() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        user.updateUsernameAndIntroduction("", null);

        // then
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO_1);
    }

    @Test
    @DisplayName("관심포지션_변경_있으면_결과는_changed_이다")
    void changePositions_returnsChangedWhenModified() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        UserPosition pos1 = new UserPosition(TEST_GUID_1, "001");
        user.loadPositionsAndSkills(new HashSet<>(Set.of(pos1)), new HashSet<>()); // mutable Set

        UserPosition pos2 = new UserPosition(TEST_GUID_1, "002");
        Set<UserPosition> newPositions = new HashSet<>(Set.of(pos1, pos2)); // mutable Set

        // when
        UserPositionChangeResult result = user.changePositions(newPositions);

        // then
        assertThat(result.changed()).isTrue();
        assertThat(result.previousPositions()).containsExactlyInAnyOrder(pos1);
        assertThat(result.currentPositions()).containsExactlyInAnyOrder(pos1, pos2);
    }

    @Test
    @DisplayName("관심포지션_변경_없으면_unchanged_결과를_반환한다")
    void changePositions_returnsUnchangedWhenSame() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        UserPosition pos1 = new UserPosition(TEST_GUID_1, "001");
        user.loadPositionsAndSkills(Set.of(pos1), Set.of());

        // when
        UserPositionChangeResult result = user.changePositions(Set.of(pos1));

        // then
        assertThat(result.changed()).isFalse();
        assertThat(result.previousPositions()).containsExactly(pos1);
        assertThat(result.currentPositions()).containsExactly(pos1);
    }

    @Test
    @DisplayName("관심포지션_null_또는_빈값이면_unchanged_반환")
    void changePositions_returnsUnchangedWhenNullOrEmpty() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        UserPositionChangeResult nullResult = user.changePositions(null);
        UserPositionChangeResult emptyResult = user.changePositions(Set.of());

        // then
        assertThat(nullResult.changed()).isFalse();
        assertThat(emptyResult.changed()).isFalse();
    }

    @Test
    @DisplayName("보유스킬_변경_있으면_changed_결과를_반환한다")
    void changeSkills_returnsChangedWhenModified() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        UserSkill skill1 = new UserSkill(TEST_GUID_1, "SKILL_001");
        user.loadPositionsAndSkills(Set.of(), new HashSet<>(Set.of(skill1)));

        UserSkill skill2 = new UserSkill(TEST_GUID_1, "SKILL_002");
        Set<UserSkill> newSkills = new HashSet<>(Set.of(skill1, skill2));

        // when
        UserSkillChangeResult result = user.changeSkills(newSkills);

        // then
        assertThat(result.changed()).isTrue();
        assertThat(result.previousSkills()).containsExactly(skill1);
        assertThat(result.currentSkills()).containsExactlyInAnyOrder(skill1, skill2);
    }

    @Test
    @DisplayName("보유스킬_변경_없으면_unchanged_결과를_반환한다")
    void changeSkills_returnsUnchangedWhenSame() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        UserSkill skill1 = new UserSkill(TEST_GUID_1, "SKILL_001");
        user.loadPositionsAndSkills(Set.of(), Set.of(skill1));

        // when
        UserSkillChangeResult result = user.changeSkills(Set.of(skill1));

        // then
        assertThat(result.changed()).isFalse();
        assertThat(result.previousSkills()).containsExactly(skill1);
        assertThat(result.currentSkills()).containsExactly(skill1);
    }

    @Test
    @DisplayName("보유스킬_null_또는_빈값이면_unchanged_반환")
    void changeSkills_returnsUnchangedWhenNullOrEmpty() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        UserSkillChangeResult nullResult = user.changeSkills(null);
        UserSkillChangeResult emptyResult = user.changeSkills(Set.of());

        // then
        assertThat(nullResult.changed()).isFalse();
        assertThat(emptyResult.changed()).isFalse();
    }

    @Test
    @DisplayName("loadPositionsAndSkills_로_포지션과_스킬을_초기화한다")
    void loadPositionsAndSkills_initializesPositionsAndSkills() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        UserPosition pos = new UserPosition(TEST_GUID_1, "001");
        UserSkill skill = new UserSkill(TEST_GUID_1, "SKILL_001");

        // when
        user.loadPositionsAndSkills(Set.of(pos), Set.of(skill));

        // then
        assertThat(user.getPositions()).containsExactly(pos);
        assertThat(user.getSkills()).containsExactly(skill);
    }
}