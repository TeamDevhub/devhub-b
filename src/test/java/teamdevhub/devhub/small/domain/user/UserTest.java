package teamdevhub.devhub.small.domain.user;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.small.common.mock.constant.TestConstant.*;

class UserTest {

    @Test
    void 관리자_계정은_관심_포지션과_보유_스킬이_빈_값이다() {
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

    @Test
    void 관심_포지션과_보유_스킬을_리스트로_받고_일반_유저를_생성한다() {
        // given,when
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // then
        assertThat(user.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(user.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO);
        assertThat(user.getPositions()).isEqualTo(TEST_POSITIONS);
        assertThat(user.getSkills()).isEqualTo(TEST_SKILLS);
        assertThat(user.isDeleted()).isFalse();
        assertThat(user.isBlocked()).isFalse();
        assertThat(user.getMannerDegree()).isEqualTo(36.5);
    }

    @Test
    void 이메일_값이_공백이면_예외를_던진다() {

        assertThatThrownBy(
                // given,when
                () -> User.createGeneralUser(TEST_GUID, "", TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST)
        )
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("사용자 ID 값이 잘못되었습니다.");
    }

    @Test
    void 비밀번호_길이가_8자보다_짧으면_예외를_던진다() {

        assertThatThrownBy(
                // given,when
                () -> User.createGeneralUser(TEST_GUID, TEST_EMAIL, "123456", TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST)
        )
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("사용자 비밀번호 값이 잘못되었습니다.");
    }

    @Test
    void 관심_포지션이_빈값이면_예외를_던진다() {

        assertThatThrownBy(
                // given,when
                () -> User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, null, TEST_SKILL_LIST)
        )
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("관심 포지션은 필수입니다.");
    }

    @Test
    void 관리자_계정은_보유_스킬이_빈_값이어도_예외를_던지지_않는다() {
        // given, when
        User adminUser = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        // then
        assertThat(adminUser.getUserRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void 탈퇴한_회원은_deleted_값이_true_다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        user.withdraw();

        // then
        assertThat(user.isDeleted()).isTrue();
        assertThat(user.isBlocked()).isFalse();
    }

    @Test
    void 이미_탈퇴한_회원이_재탈퇴를_요청하면_예외를_던진다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        user.withdraw();

        // when
        assertThatThrownBy(user::withdraw)
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("이미 탈퇴한 회원입니다.");
    }

    @Test
    void 사용자_프로필_정보를_새로운_값으로_변경하면_기존_값이_변경된다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        user.updateProfile(NEW_USERNAME, NEW_INTRO, NEW_POSITIONS, NEW_SKILLS);

        // then
        assertThat(user.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(user.getIntroduction()).isEqualTo(NEW_INTRO);
        assertThat(user.getPositions()).isEqualTo(NEW_POSITIONS);
        assertThat(user.getSkills()).isEqualTo(NEW_SKILLS);
    }

    @Test
    void 빈_값_또는_이전과_같은_값으로_변경하면_기존_값은_변경되지_않는다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        user.updateProfile("", null, TEST_POSITIONS, NEW_SKILLS);

        // then
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO);
        assertThat(user.getPositions()).isEqualTo(TEST_POSITIONS);
        assertThat(user.getSkills()).isEqualTo(NEW_SKILLS);
    }

    @Test
    void 관심_스킬을_빈_값으로_변경하면_예외를_던진다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        assertThatThrownBy(() -> user.updateProfile(NEW_USERNAME, NEW_INTRO, NEW_POSITIONS, null))
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("보유 스킬목록은 필수입니다.");
    }
}