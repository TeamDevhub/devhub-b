package teamdevhub.devhub.small.domain.mail;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.domain.mail.EmailVerification;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class EmailVerificationTest {

    @Test
    void issue_로_생성하면_verified_는_false_이다() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        // when
        EmailVerification emailVerification = EmailVerification.issue("test@test.com", "123456", expiredAt);

        // then
        assertThat(emailVerification.isVerified()).isFalse();
        assertThat(emailVerification.getEmail()).isEqualTo("test@test.com");
        assertThat(emailVerification.getCode()).isEqualTo("123456");
        assertThat(emailVerification.getExpiredAt()).isEqualTo(expiredAt);
    }

    @Test
    void 만료되지_않았고_코드가_일치하면_인증에_성공한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification =
                EmailVerification.issue(
                        "test@test.com",
                        "123456",
                        now.plusMinutes(5)
                );

        // when
        boolean result = emailVerification.verify("123456", now);

        // then
        assertThat(result).isTrue();
        assertThat(emailVerification.isVerified()).isTrue();
    }

    @Test
    void 코드가_일치하지_않으면_인증에_실패한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification =
                EmailVerification.issue(
                        "test@test.com",
                        "123456",
                        now.plusMinutes(5)
                );

        // when
        boolean result = emailVerification.verify("999999", now);

        // then
        assertThat(result).isFalse();
        assertThat(emailVerification.isVerified()).isFalse();
    }

    @Test
    void 만료되었으면_인증에_실패한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification =
                EmailVerification.issue(
                        "test@test.com",
                        "123456",
                        now.minusMinutes(1)
                );

        // when
        boolean result = emailVerification.verify("123456", now);

        // then
        assertThat(result).isFalse();
        assertThat(emailVerification.isVerified()).isFalse();
    }

    @Test
    void 이미_verified_된_객체는_상태가_true_이다() {
        // given
        EmailVerification emailVerification =
                EmailVerification.from(
                        "test@test.com",
                        "123456",
                        LocalDateTime.now().plusMinutes(5),
                        true
                );

        // when, then
        assertThat(emailVerification.isVerified()).isTrue();
    }

    @Test
    void expiredAt_이_현재시간보다_이전이면_만료이다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification =
                EmailVerification.issue(
                        "test@test.com",
                        "123456",
                        now.minusSeconds(1)
                );

        // when, then
        assertThat(emailVerification.isExpired(now)).isTrue();
    }

    @Test
    void expiredAt_이_현재시간보다_이후면_만료가_아니다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailVerification emailVerification =
                EmailVerification.issue(
                        "test@test.com",
                        "123456",
                        now.plusSeconds(1)
                );

        // when, then
        assertThat(emailVerification.isExpired(now)).isFalse();
    }
}