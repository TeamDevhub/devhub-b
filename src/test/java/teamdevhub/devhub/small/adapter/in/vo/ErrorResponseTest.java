package teamdevhub.devhub.small.adapter.in.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.web.model.response.ErrorResponse;
import teamdevhub.devhub.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    @DisplayName("ErrorCode_로부터_ErrorResponseVo_를_생성한다")
    void createResponseVoFromErrorCode() {
        // given
        ErrorCode errorCode = ErrorCode.READ_FAIL;

        // when
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);

        // then
        assertThat(errorResponse.getCode()).isEqualTo(errorCode.getCode());
        assertThat(errorResponse.getMessage()).isEqualTo(errorCode.getMessage());
    }

    @Test
    @DisplayName("코드와_메시지를_직접_지정하여_ErrorResponseVo_를_생성한다")
    void createResponseVoWithCustomCodeAndMessage() {
        // given
        String code = ErrorCode.READ_FAIL.getCode();
        String message = ErrorCode.READ_FAIL.getMessage();

        // when
        ErrorResponse errorResponse = ErrorResponse.of(code, message);

        // then
        assertThat(errorResponse.getCode()).isEqualTo(code);
        assertThat(errorResponse.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Throwable_로부터_ErrorResponseVo_를_생성한다")
    void createResponseVoFromThrowable() {
        // given
        RuntimeException runtimeException = new RuntimeException("테스트 예외 메시지");

        // when
        ErrorResponse errorResponse = ErrorResponse.of(runtimeException);

        // then
        assertThat(errorResponse.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(errorResponse.getMessage()).isEqualTo("테스트 예외 메시지");
    }

    @Test
    @DisplayName("Throwable_의_메시지가_null_이면_기본_메시지를_사용한다")
    void useDefaultMessageWhenThrowableMessageIsNull() {
        // given
        RuntimeException runtimeException = new RuntimeException((String) null);

        // when
        ErrorResponse errorResponse = ErrorResponse.of(runtimeException);

        // then
        assertThat(errorResponse.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(errorResponse.getMessage()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getMessage());
    }

    @Test
    @DisplayName("Throwable_의_메시지가_빈값이면_기본_메시지를_사용한다")
    void useDefaultMessageWhenThrowableMessageIsEmpty() {
        // given
        RuntimeException runtimeException = new RuntimeException("   ");

        // when
        ErrorResponse errorResponse = ErrorResponse.of(runtimeException);

        // then
        assertThat(errorResponse.getMessage()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getMessage());
    }

    @Test
    @DisplayName("Throwable_이_null_이면_기본_메시지를_사용한다")
    void useDefaultMessageWhenThrowableIsNull() {
        // given
        ErrorResponse errorResponse = ErrorResponse.of((Throwable) null);

        // when, then
        assertThat(errorResponse.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(errorResponse.getMessage()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getMessage());
    }
}