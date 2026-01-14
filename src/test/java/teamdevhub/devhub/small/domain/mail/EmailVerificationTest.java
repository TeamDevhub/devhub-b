package teamdevhub.devhub.small.domain.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.domain.mail.EmailVerification;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;


class EmailVerificationTest {

    @Test
    @DisplayName("이메일_인증_데이터를_처음_생성하면_verified_의_초기값은_false_이다")
    void createEmailVerificationWithIssue() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        // when
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL_1, EMAIL_CODE, expiredAt);

        // then
        assertThat(emailVerification.isVerified()).isFalse();
        assertThat(emailVerification.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(emailVerification.getCode()).isEqualTo(EMAIL_CODE);
        assertThat(emailVerification.getExpiredAt()).isEqualTo(expiredAt);
    }

    @Test
    @DisplayName("이메일_인증이_만료되지_않았고_코드가_일치하면_verified_의_값은_true_이다")
    void verifySuccessIfNotExpiredAndCodeMatches() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL_1, EMAIL_CODE, now.plusMinutes(5));

        // when
        boolean result = emailVerification.verify(EMAIL_CODE, now);

        // then
        assertThat(result).isTrue();
        assertThat(emailVerification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("인증_코드가_일치하지_않으면_verified_의_값은_false_이다")
    void verifyFailIfCodeDoesNotMatch() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL_1, EMAIL_CODE, now.plusMinutes(5));

        // when
        boolean result = emailVerification.verify("999999", now);

        // then
        assertThat(result).isFalse();
        assertThat(emailVerification.isVerified()).isFalse();
    }

    @Test
    @DisplayName("이메일_인증_만료시간이_지났으면_인증에_실패한다")
    void verifyFailIfExpired() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL_1, EMAIL_CODE, now.minusMinutes(1));

        // when
        boolean result = emailVerification.verify(EMAIL_CODE, now);

        // then
        assertThat(result).isFalse();
        assertThat(emailVerification.isVerified()).isFalse();
    }

    @Test
    @DisplayName("이미_verified_된_이메일_인증의_verified_값은_true_이다")
    void isAlreadyVerified() {
        // given
        EmailVerification emailVerification = EmailVerification.from(TEST_EMAIL_1, EMAIL_CODE, LocalDateTime.now().plusMinutes(5), true);

        // when, then
        assertThat(emailVerification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("expiredAt_의_값이_현재시간보다_이전이면_만료된_상태이다")
    void isExpiredIfBeforeNow() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL_1, EMAIL_CODE, now.minusSeconds(1));

        // when, then
        assertThat(emailVerification.isExpired(now)).isTrue();
    }

    @Test
    @DisplayName("expiredAt_이_현재시간보다_이후면_만료된_상태가_아니다")
    void isNotExpiredIfAfterNow() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL_1, EMAIL_CODE, now.plusSeconds(1));

        // when, then
        assertThat(emailVerification.isExpired(now)).isFalse();
    }
}