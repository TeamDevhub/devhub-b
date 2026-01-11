package teamdevhub.devhub.small.adapter.out.common.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.common.converter.BooleanToYNConverter;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.common.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BooleanToYNConverterTest {

    private final BooleanToYNConverter booleanToYNConverter = new BooleanToYNConverter();

    @Test
    @DisplayName("true_는_Y_로_변환된다")
    void convertTrueToY() {
        // given, when
        String result = booleanToYNConverter.convertToDatabaseColumn(true);

        // then
        assertThat(result).isEqualTo("Y");
    }

    @Test
    @DisplayName("false_는_N_으로_변환된다")
    void convertFalseToN() {
        // given, when
        String result = booleanToYNConverter.convertToDatabaseColumn(false);

        // then
        assertThat(result).isEqualTo("N");
    }

    @Test
    @DisplayName("null_은_N_으로_변환된다")
    void convertNullToN() {
        // given, when
        String result = booleanToYNConverter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isEqualTo("N");
    }

    @Test
    @DisplayName("Y_는_true_로_변환된다")
    void convertYToTrue() {
        // given, when
        Boolean result = booleanToYNConverter.convertToEntityAttribute("Y");

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("N_은_false_로_변환된다")
    void convertNToFalse() {
        // given, when
        Boolean result = booleanToYNConverter.convertToEntityAttribute("N");

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("null_은_false_로_변환된다")
    void convertNullToFalse() {
        // given, when
        Boolean result = booleanToYNConverter.convertToEntityAttribute(null);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Y_N_외의_값이면_예외가_발생한다")
    void throwIfValueIsNotYOrN() {
        // given
        String invalidValue = "X";

        // then
        assertThatThrownBy(
                // when
                () -> booleanToYNConverter.convertToEntityAttribute(invalidValue))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.BOOLEAN_CONVERT_FAIL.getMessage());
    }
}