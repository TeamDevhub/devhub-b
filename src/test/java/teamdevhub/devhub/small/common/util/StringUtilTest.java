package teamdevhub.devhub.small.common.util;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.common.util.StringUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringUtilTest {

    @Test
    void 문자열이_null_이거나_빈_문자열이면_isEmpty_는_true_를_반환한다() {
        // given, when, then
        assertThat(StringUtil.isEmpty(null)).isTrue();
        assertThat(StringUtil.isEmpty("")).isTrue();
        assertThat(StringUtil.isEmpty("   ")).isTrue();
    }

    @Test
    void 문자열에_내용이_있으면_isEmpty_는_false_를_반환한다() {
        // given, when, then
        assertThat(StringUtil.isEmpty("A")).isFalse();
        assertThat(StringUtil.isEmpty(" hello ")).isFalse();
    }

    @Test
    void 문자열이_null_이거나_빈문자열이면_isNotEmpty_는_false_를_반환한다() {
        // given, when, then
        assertThat(StringUtil.isNotEmpty(null)).isFalse();
        assertThat(StringUtil.isNotEmpty("")).isFalse();
        assertThat(StringUtil.isNotEmpty("   ")).isFalse();
    }

    @Test
    void 문자열에_내용이_있으면_isNotEmpty_는_true_를_반환한다() {
        // given, when, then
        assertThat(StringUtil.isNotEmpty("A")).isTrue();
        assertThat(StringUtil.isNotEmpty(" hello ")).isTrue();
    }

    @Test
    void 문자열이_null_이거나_빈문자열이면_defaultIfEmpty_는_기본값을_반환한다() {
        // given, when, then
        assertThat(StringUtil.defaultIfEmpty(null, "default")).isEqualTo("default");
        assertThat(StringUtil.defaultIfEmpty("", "default")).isEqualTo("default");
        assertThat(StringUtil.defaultIfEmpty("   ", "default")).isEqualTo("default");
    }

    @Test
    void 문자열에_내용이_있으면_defaultIfEmpty_는_원본문자열을_반환한다() {
        // given, when, then
        assertThat(StringUtil.defaultIfEmpty("hello", "default")).isEqualTo("hello");
        assertThat(StringUtil.defaultIfEmpty(" world ", "default")).isEqualTo(" world ");
    }

    @Test
    void 문자열_길이가_maxLength_보다_작거나_같으면_truncate_는_원본문자열을_반환한다() {
        // given, when, then
        assertThat(StringUtil.truncate("abc", 5)).isEqualTo("abc");
        assertThat(StringUtil.truncate("abc", 3)).isEqualTo("abc");
    }

    @Test
    void 문자열_길이가_maxLength_보다_크면_truncate_는_maxLength_까지_문자열을_잘라서_반환한다() {
        // given, when, then
        assertThat(StringUtil.truncate("abcdef", 3)).isEqualTo("abc");
        assertThat(StringUtil.truncate(" hello world ", 5)).isEqualTo(" hell");
    }

    @Test
    void maxLength_가_0보다_작으면_truncate_는_예외를_던진다() {
        // given, when, then
        assertThatThrownBy(() -> StringUtil.truncate("abc", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("maxLength must be positive");
    }

    @Test
    void 문자열이_null_이면_truncate_는_null_을_반환한다() {
        // given, when, then
        assertThat(StringUtil.truncate(null, 5)).isNull();
    }

    @Test
    void 문자열이_빈_문자열이면_truncate_는_빈_문자열을_반환한다() {
        // given, when, then
        assertThat(StringUtil.truncate("", 5)).isEmpty();
        assertThat(StringUtil.truncate("   ", 2)).isEqualTo("   ");
    }
}