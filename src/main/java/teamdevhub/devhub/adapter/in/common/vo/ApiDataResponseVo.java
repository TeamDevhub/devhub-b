package teamdevhub.devhub.adapter.in.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.SuccessCode;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataResponseVo<T> {

    private boolean success;

    private String code;

    private T data;

    private ErrorResponseVo error;

    public static <T> ApiDataResponseVo<T> successWithData(SuccessCode successCode, T data) {
        return ApiDataResponseVo.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .data(data)
                .build();
    }

    public static ApiDataResponseVo<Void> successWithoutData(SuccessCode successCode) {
        return ApiDataResponseVo.<Void>builder()
                .success(true)
                .code(successCode.getCode())
                .build();
    }

    public static <T> ApiDataResponseVo<T> failureWithData(ErrorCode errorCode, T data) {
        return ApiDataResponseVo.<T>builder()
                .success(false)
                .code(errorCode.getCode())
                .data(data)
                .error(ErrorResponseVo.of(errorCode))
                .build();
    }
    
    public static <T> ApiDataResponseVo<T> failureWithoutData(ErrorCode errorCode) {
        return ApiDataResponseVo.<T>builder()
                .success(false)
                .code(errorCode.getCode())
                .error(ErrorResponseVo.of(errorCode))
                .build();
    }

    public static ApiDataResponseVo<?> failureWithMessage(ErrorCode errorCode, String message) {
        return ApiDataResponseVo.builder()
                .success(false)
                .code(errorCode.getCode())
                .error(new ErrorResponseVo(errorCode.getCode(), message))
                .build();
    }

    public static <T> ApiDataResponseVo<T> failureFromThrowable(Throwable throwable) {
        return ApiDataResponseVo.<T>builder()
                .success(false)
                .code(ErrorCode.UNKNOWN_FAIL.getCode())
                .error(ErrorResponseVo.of(throwable))
                .build();
    }

    public static <T> ApiDataResponseVo<T> failureFromFilter(Throwable throwable) {
        return ApiDataResponseVo.<T>builder()
                .success(false)
                .code(ErrorCode.UNKNOWN_FAIL.getCode())
                .error(ErrorResponseVo.of(throwable))
                .build();
    }
}