package teamdevhub.devhub.small.core.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserActiveAndReviewTest {

    private User buildGeneralUser(String userGuid) {
        SignupUserCommand command = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST).verificationTarget(VERIFICATION_TARGET_1).build();
        return User.createGeneralUser(CreateUserCommand.generalUserCreateCommand(command, userGuid));
    }

    private User buildBlockedUser(String userGuid) {
        return User.of(userGuid, UserRole.USER, TEST_USERNAME_1, TEST_INTRO_1,
                null, 36.5, true, TEST_BLOCK_END_DATE, false, LocalDateTime.now(), AuditInfo.empty());
    }

    private User buildDeletedUser(String userGuid) {
        User user = buildGeneralUser(userGuid);
        user.withdraw();
        return user;
    }

    // --- assertActive ---

    @Test
    @DisplayName("활성_상태의_사용자는_assertActive_를_호출해도_예외가_발생하지_않는다")
    void assertActive_activeUser_noException() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);

        // when, then — no exception
        user.assertActive();
    }

    @Test
    @DisplayName("탈퇴한_사용자는_assertActive_를_호출하면_USER_WITHDRAWN_예외가_발생한다")
    void assertActive_withdrawnUser_throwsDomainRuleException() {
        // given
        User user = buildDeletedUser(TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(user::assertActive)
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_WITHDRAWN.getMessage());
    }

    @Test
    @DisplayName("정지된_사용자는_assertActive_를_호출하면_USER_BLOCKED_예외가_발생한다")
    void assertActive_blockedUser_throwsDomainRuleException() {
        // given
        User user = buildBlockedUser(TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(user::assertActive)
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_BLOCKED.getMessage());
    }

    // --- applyReviewScore ---

    @Test
    @DisplayName("리뷰_점수_3_0을_적용하면_매너도가_변하지_않는다")
    void applyReviewScore_score3_mannerUnchanged() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(3.0);

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner);
    }

    @Test
    @DisplayName("리뷰_점수_5_0을_적용하면_매너도가_2_0_증가한다")
    void applyReviewScore_score5_increasesManner() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(5.0);

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner + 2.0);
    }

    @Test
    @DisplayName("리뷰_점수_1_0을_적용하면_매너도가_2_0_감소한다")
    void applyReviewScore_score1_decreasesManner() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(1.0);

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner - 2.0);
    }

    @Test
    @DisplayName("리뷰_점수를_여러번_적용하면_누적된다")
    void applyReviewScore_multipleTimes_accumulates() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(5.0); // +2.0
        user.applyReviewScore(1.0); // -2.0

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner);
    }

    @Test
    @DisplayName("리뷰_점수_4_5를_적용하면_매너도가_1_5_증가한다")
    void applyReviewScore_score4_5_increasesManner() {
        // given
        User user = buildGeneralUser(TEST_USER_GUID_1);
        double initialManner = user.getMannerDegree();

        // when
        user.applyReviewScore(4.5);

        // then
        assertThat(user.getMannerDegree()).isEqualTo(initialManner + 1.5);
    }
}
