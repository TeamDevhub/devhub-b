package teamdevhub.devhub.small.common.provider;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamdevhub.devhub.common.provider.datetime.SystemDateTimeProvider;
import teamdevhub.devhub.common.provider.password.SystemPasswordPolicyProvider;
import teamdevhub.devhub.common.provider.uuid.SystemIdentifierProvider;
import teamdevhub.devhub.common.provider.verification.SystemEmailVerificationCodeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SystemProvidersTest {

    @Test
    void 이메일_인증코드를_생성하면_6자리_숫자로_반환된다() {
        // given
        SystemEmailVerificationCodeProvider provider = new SystemEmailVerificationCodeProvider();

        // when
        String code = provider.generateEmailVerificationCode();

        // then
        assertThat(code).isNotNull();
        assertThat(code).hasSize(6);
        assertThat(code).matches("\\d{6}");
    }

    @Test
    void 식별자를_생성하면_UUID_기반_32자리_문자열로_반환된다() {
        // given
        SystemIdentifierProvider provider = new SystemIdentifierProvider();

        // when
        String id = provider.generateIdentifier();

        // then
        assertThat(id).isNotNull();
        assertThat(id).doesNotContain("-");
        assertThat(id.length()).isEqualTo(32);
    }

    @Test
    void 비밀번호를_암호화하면_일치검증이_정상적으로_동작한다() {
        //given
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // when
        SystemPasswordPolicyProvider provider = new SystemPasswordPolicyProvider(passwordEncoder);
        String rawPassword = "test1234!";
        String hashedPassword = provider.encode(rawPassword);

        // then
        assertThat(hashedPassword).isNotNull();
        assertThat(provider.matches(rawPassword, hashedPassword)).isTrue();
        assertThat(provider.matches("틀린비밀번호", hashedPassword)).isFalse();
    }

    @Test
    void 날짜를_포맷하고_다시_파싱하면_원본과_동일하다() {
        // given
        SystemDateTimeProvider provider = new SystemDateTimeProvider();
        LocalDate today = LocalDate.of(2026, 1, 9);
        String pattern = "yyyy-MM-dd";

        // when
        String formatted = provider.formatDate(today, pattern);
        LocalDate parsed = provider.parseDate(formatted, pattern);

        // then
        assertThat(formatted).isEqualTo("2026-01-09");
        assertThat(parsed).isEqualTo(today);
    }

    @Test
    void 날짜시간을_포맷하고_다시_파싱하면_원본과_동일하다() {
        // given
        SystemDateTimeProvider provider = new SystemDateTimeProvider();
        LocalDateTime now = LocalDateTime.of(2026, 1, 9, 14, 45, 30);
        String pattern = "yyyy-MM-dd HH:mm:ss";

        // when
        String formatted = provider.formatDateTime(now, pattern);
        LocalDateTime parsed = provider.parseDateTime(formatted, pattern);

        // then
        assertThat(formatted).isEqualTo("2026-01-09 14:45:30");
        assertThat(parsed).isEqualTo(now);
    }

    @Test
    void today_와_now_메서드는_현재_날짜와_시간을_반환한다() {
        // given
        SystemDateTimeProvider provider = new SystemDateTimeProvider();

        // when
        LocalDate today = provider.today();
        LocalDateTime now = provider.now();

        // then
        assertThat(today).isNotNull();
        assertThat(now).isNotNull();
    }
}
