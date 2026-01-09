package teamdevhub.devhub.small.adapter.in.vo;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.vo.ErrorResponseVo;
import teamdevhub.devhub.common.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseVoTest {

    @Test
    void ErrorCode_로부터_ErrorResponseVo_를_생성한다() {
        // given
        ErrorCode errorCode = ErrorCode.READ_FAIL;

        // when
        ErrorResponseVo response = ErrorResponseVo.of(errorCode);

        // then
        assertThat(response.getCode()).isEqualTo(errorCode.getCode());
        assertThat(response.getMessage()).isEqualTo(errorCode.getMessage());
    }

    @Test
    void 코드와_메시지를_직접_지정하여_ErrorResponseVo_를_생성한다() {
        // given
        String code = "CUSTOM_ERROR";
        String message = "Custom error message";

        // when
        ErrorResponseVo response = ErrorResponseVo.of(code, message);

        // then
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
    }

    @Test
    void Throwable_로부터_ErrorResponseVo_를_생성한다() {
        // given
        RuntimeException exception = new RuntimeException("예외 메시지");

        // when
        ErrorResponseVo response = ErrorResponseVo.of(exception);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(response.getMessage()).isEqualTo("예외 메시지");
    }

    @Test
    void Throwable_의_메시지가_null_이면_기본_메시지를_사용한다() {
        // given
        RuntimeException exception = new RuntimeException((String) null);

        // when
        ErrorResponseVo response = ErrorResponseVo.of(exception);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(response.getMessage())
                .isEqualTo("Unexpected system error occurred");
    }

    @Test
    void Throwable_의_메시지가_빈값이면_기본_메시지를_사용한다() {
        // given
        RuntimeException exception = new RuntimeException("   ");

        // when
        ErrorResponseVo response = ErrorResponseVo.of(exception);

        // then
        assertThat(response.getMessage())
                .isEqualTo("Unexpected system error occurred");
    }

    @Test
    void Throwable_이_null_이면_기본_메시지를_사용한다() {
        // when
        ErrorResponseVo response = ErrorResponseVo.of((Throwable) null);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(response.getMessage())
                .isEqualTo("Unexpected system error occurred");
    }
}