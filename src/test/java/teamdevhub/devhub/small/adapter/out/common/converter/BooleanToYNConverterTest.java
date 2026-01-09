package teamdevhub.devhub.small.adapter.out.common.converter;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.common.converter.BooleanToYNConverter;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.common.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BooleanToYNConverterTest {

    private final BooleanToYNConverter converter = new BooleanToYNConverter();

    @Test
    void true_는_Y_로_변환된다() {
        // given, when
        String result = converter.convertToDatabaseColumn(true);

        // then
        assertThat(result).isEqualTo("Y");
    }

    @Test
    void false_는_N_으로_변환된다() {
        // given, when
        String result = converter.convertToDatabaseColumn(false);

        // then
        assertThat(result).isEqualTo("N");
    }

    @Test
    void null_은_N_으로_변환된다() {
        // given, when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isEqualTo("N");
    }

    @Test
    void Y_는_true_로_변환된다() {
        // given, when
        Boolean result = converter.convertToEntityAttribute("Y");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void N_은_false_로_변환된다() {
        // given, when
        Boolean result = converter.convertToEntityAttribute("N");

        // then
        assertThat(result).isFalse();
    }

    @Test
    void null_은_false_로_변환된다() {
        // given, when
        Boolean result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void Y_N_외의_값이면_예외가_발생한다() {
        // given
        String invalidValue = "X";

        // then
        assertThatThrownBy(
                // when
                () -> converter.convertToEntityAttribute(invalidValue))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.BOOLEAN_CONVERT_FAIL.getMessage());
    }
}