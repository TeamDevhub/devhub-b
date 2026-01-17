package teamdevhub.devhub.small.domain.verification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.verification.Verification;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class VerificationTest {

    @Test
    @DisplayName("인증을_발급하면_미인증_상태이다")
    void issueVerification_isUnverified() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        // when
        Verification verification = Verification.issue(VERIFICATION_TARGET_1, EMAIL_CODE, expiredAt);

        // then
        assertThat(verification.isVerified()).isFalse();
        assertThat(verification.getVerificationTarget()).isEqualTo(VERIFICATION_TARGET_1);
        assertThat(verification.getCode()).isEqualTo(EMAIL_CODE);
        assertThat(verification.getExpiredAt()).isEqualTo(expiredAt);
    }

    @Test
    @DisplayName("올바른_코드이고_만료되지_않았다면_인증에_성공한다")
    void confirm_success_whenCodeIsCorrectAndNotExpired() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Verification verification = Verification.issue(VERIFICATION_TARGET_1, EMAIL_CODE, now.plusMinutes(3));

        // when
        verification.confirm(EMAIL_CODE, now);

        // then
        assertThat(verification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("잘못된_인증코드를_입력하면_예외를_던진다")
    void throwException_whenCodeIsInvalid() {
        // given
        Verification verification = Verification.issue(VERIFICATION_TARGET_1, EMAIL_CODE, LocalDateTime.now().plusMinutes(3));

        // then
        assertThatThrownBy(
                // when
                () -> verification.confirm("000000", LocalDateTime.now()))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("만료된_인증코드를_확인하면_예외를_던진다")
    void throwException_whenVerificationExpired() {
        // given
        Verification verification = Verification.issue(VERIFICATION_TARGET_1, EMAIL_CODE, LocalDateTime.now().minusSeconds(1));

        // then
        assertThatThrownBy(
                // when
                () -> verification.confirm(EMAIL_CODE, LocalDateTime.now()))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("이미_인증된_상태에서_다시_인증해도_성공한다")
    void confirmAgain_whenAlreadyVerified() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Verification verification = Verification.issue(VERIFICATION_TARGET_1, EMAIL_CODE, now.plusMinutes(3));
        verification.confirm(EMAIL_CODE, now);

        // when
        verification.confirm(EMAIL_CODE, now.plusMinutes(1));

        // then
        assertThat(verification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("인증되고_만료되지_않았다면_assertValid_을_통과한다")
    void assertValid_success_whenVerifiedAndNotExpired() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Verification verification = Verification.issue(VERIFICATION_TARGET_1, EMAIL_CODE, now.plusMinutes(3));
        verification.confirm(EMAIL_CODE, now);

        // then
        assertThatCode(
                // when
                () -> verification.assertValid(now.plusMinutes(1)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("미인증_상태라면_assertValid_에서_예외를_던진다")
    void throwException_whenAssertValidAndNotVerified() {
        // given
        Verification verification = Verification.issue(VERIFICATION_TARGET_1, EMAIL_CODE, LocalDateTime.now().plusMinutes(3));

        // then
        assertThatThrownBy(
                // when
                () -> verification.assertValid(LocalDateTime.now()))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("인증되었어도_만료되었다면_assertValid_에서_예외를_던진다")
    void throwException_whenAssertValidAndExpired() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Verification verification = Verification.issue(VERIFICATION_TARGET_1, EMAIL_CODE, now.plusMinutes(1));

        verification.confirm(EMAIL_CODE, now);

        // then
        assertThatThrownBy(
                // when
                () -> verification.assertValid(now.plusMinutes(2)))
                .isInstanceOf(DomainRuleException.class);
    }
}
