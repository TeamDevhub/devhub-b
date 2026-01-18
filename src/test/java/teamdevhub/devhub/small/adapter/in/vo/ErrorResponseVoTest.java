package teamdevhub.devhub.small.adapter.in.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.vo.ErrorResponseVo;
import teamdevhub.devhub.common.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseVoTest {

    @Test
    @DisplayName("ErrorCode_로부터_ErrorResponseVo_를_생성한다")
    void createResponseVoFromErrorCode() {
        // given
        ErrorCode errorCode = ErrorCode.READ_FAIL;

        // when
        ErrorResponseVo errorResponseVo = ErrorResponseVo.of(errorCode);

        // then
        assertThat(errorResponseVo.getCode()).isEqualTo(errorCode.getCode());
        assertThat(errorResponseVo.getMessage()).isEqualTo(errorCode.getMessage());
    }

    @Test
    @DisplayName("코드와_메시지를_직접_지정하여_ErrorResponseVo_를_생성한다")
    void createResponseVoWithCustomCodeAndMessage() {
        // given
        String code = ErrorCode.READ_FAIL.getCode();
        String message = ErrorCode.READ_FAIL.getMessage();;

        // when
        ErrorResponseVo errorResponseVo = ErrorResponseVo.of(code, message);

        // then
        assertThat(errorResponseVo.getCode()).isEqualTo(code);
        assertThat(errorResponseVo.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Throwable_로부터_ErrorResponseVo_를_생성한다")
    void createResponseVoFromThrowable() {
        // given
        RuntimeException runtimeException = new RuntimeException("테스트 예외 메시지");

        // when
        ErrorResponseVo errorResponseVo = ErrorResponseVo.of(runtimeException);

        // then
        assertThat(errorResponseVo.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(errorResponseVo.getMessage()).isEqualTo("테스트 예외 메시지");
    }

    @Test
    @DisplayName("Throwable_의_메시지가_null_이면_기본_메시지를_사용한다")
    void useDefaultMessageWhenThrowableMessageIsNull() {
        // given
        RuntimeException runtimeException = new RuntimeException((String) null);

        // when
        ErrorResponseVo errorResponseVo = ErrorResponseVo.of(runtimeException);

        // then
        assertThat(errorResponseVo.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(errorResponseVo.getMessage()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getMessage());
    }

    @Test
    @DisplayName("Throwable_의_메시지가_빈값이면_기본_메시지를_사용한다")
    void useDefaultMessageWhenThrowableMessageIsEmpty() {
        // given
        RuntimeException runtimeException = new RuntimeException("   ");

        // when
        ErrorResponseVo errorResponseVo = ErrorResponseVo.of(runtimeException);

        // then
        assertThat(errorResponseVo.getMessage()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getMessage());
    }

    @Test
    @DisplayName("Throwable_이_null_이면_기본_메시지를_사용한다")
    void useDefaultMessageWhenThrowableIsNull() {
        // given
        ErrorResponseVo errorResponseVo = ErrorResponseVo.of((Throwable) null);

        // when, then
        assertThat(errorResponseVo.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(errorResponseVo.getMessage()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getMessage());
    }
}