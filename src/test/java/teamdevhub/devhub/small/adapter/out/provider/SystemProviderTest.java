package teamdevhub.devhub.small.adapter.out.provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.provider.time.SystemDateTimeProvider;
import teamdevhub.devhub.adapter.out.provider.identifier.SystemIdentifierProvider;
import teamdevhub.devhub.adapter.out.provider.verification.SystemVerificationCodeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SystemProviderTest {

    @Test
    @DisplayName("이메일_인증코드를_생성하면_6자리_숫자로_반환된다")
    void generateEmailVerificationCodeReturns6DigitNumber() {
        // given
        SystemVerificationCodeProvider provider = new SystemVerificationCodeProvider();

        // when
        String code = provider.generateVerificationCode();

        // then
        assertThat(code).isNotNull();
        assertThat(code).hasSize(6);
        assertThat(code).matches("\\d{6}");
    }

    @Test
    @DisplayName("식별자를_생성하면_UUID_기반_32자리_문자열로_반환된다")
    void generateIdentifierReturns32CharUUID() {
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
    @DisplayName("날짜를_포맷하고_다시_파싱하면_원본과_동일하다")
    void formatAndParseDateReturnsOriginal() {
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
    @DisplayName("날짜시간을_포맷하고_다시_파싱하면_원본과_동일하다")
    void formatAndParseDateTimeReturnsOriginal() {
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
    @DisplayName("today_와_now_메서드는_현재_날짜와_시간을_반환한다")
    void todayAndNowReturnCurrentDateTime() {
        // given
        SystemDateTimeProvider provider = new SystemDateTimeProvider();

        // when
        LocalDate today = provider.today();
        LocalDateTime now = provider.now();

        // then
        assertThat(today).isNotNull();
        assertThat(now).isNotNull();
    }

    @Test
    @DisplayName("날짜가_null_이면_formatDate_는_빈문자열을_반환한다")
    void formatDateReturnsEmptyStringWhenDateIsNull() {
        // given
        SystemDateTimeProvider provider = new SystemDateTimeProvider();
        String pattern = "yyyy-MM-dd";

        // when
        String result = provider.formatDate(null, pattern);

        // then
        assertThat(result).isEqualTo("");
    }

    @Test
    @DisplayName("날짜시간이_null_이면_formatDateTime_은_빈문자열을_반환한다")
    void formatDateTimeReturnsEmptyStringWhenDateTimeIsNull() {
        // given
        SystemDateTimeProvider provider = new SystemDateTimeProvider();
        String pattern = "yyyy-MM-dd HH:mm:ss";

        // when
        String result = provider.formatDateTime(null, pattern);

        // then
        assertThat(result).isEqualTo("");
    }

    @Test
    @DisplayName("날짜문자열이_null_이거나_빈값이면_parseDate_는_null_을_반환한다")
    void parseDateReturnsNullWhenInputIsEmpty() {
        // given
        SystemDateTimeProvider provider = new SystemDateTimeProvider();
        String pattern = "yyyy-MM-dd";

        // when, then
        assertThat(provider.parseDate(null, pattern)).isNull();
        assertThat(provider.parseDate("", pattern)).isNull();
    }

    @Test
    @DisplayName("날짜시간문자열이_null_이거나_빈값이면_parseDateTime_은_null_을_반환한다")
    void parseDateTimeReturnsNullWhenInputIsEmpty() {
        // given
        SystemDateTimeProvider provider = new SystemDateTimeProvider();
        String pattern = "yyyy-MM-dd HH:mm:ss";

        // when, then
        assertThat(provider.parseDateTime(null, pattern)).isNull();
        assertThat(provider.parseDateTime("", pattern)).isNull();
    }
}
