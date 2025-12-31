package teamdevhub.devhub.adapter.in.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataResponseVo<T> {

    private boolean success;
    private String code;
    private T data;
    private ErrorResponseVo error;

    public static <T> ApiDataResponseVo<T> successWithData(SuccessCodeEnum successCodeEnum, T data) {
        return ApiDataResponseVo.<T>builder()
                .success(true)
                .code(successCodeEnum.getCode())
                .data(data)
                .build();
    }

    public static ApiDataResponseVo<Void> successWithoutData(SuccessCodeEnum successCodeEnum) {
        return ApiDataResponseVo.<Void>builder()
                .success(true)
                .code(successCodeEnum.getCode())
                .build();
    }

    public static <T> ApiDataResponseVo<T> failureWithData(ErrorCodeEnum errorCodeEnum, T data) {
        return ApiDataResponseVo.<T>builder()
                .success(false)
                .code(errorCodeEnum.getCode())
                .data(data)
                .error(ErrorResponseVo.of(errorCodeEnum))
                .build();
    }
    
    public static <T> ApiDataResponseVo<T> failureWithoutData(ErrorCodeEnum errorCodeEnum) {
        return ApiDataResponseVo.<T>builder()
                .success(false)
                .code(errorCodeEnum.getCode())
                .error(ErrorResponseVo.of(errorCodeEnum))
                .build();
    }

    public static ApiDataResponseVo<?> failureWithMessage(ErrorCodeEnum errorCodeEnum, String message) {
        return ApiDataResponseVo.builder()
                .success(false)
                .code(errorCodeEnum.getCode())
                .error(new ErrorResponseVo(errorCodeEnum.getCode(), message))
                .build();
    }

    public static <T> ApiDataResponseVo<T> failureFromThrowable(Throwable throwable) {
        return ApiDataResponseVo.<T>builder()
                .success(false)
                .code(ErrorCodeEnum.UNKNOWN_FAIL.getCode())
                .error(ErrorResponseVo.of(throwable))
                .build();
    }

    public static <T> ApiDataResponseVo<T> failureFromFilter(Throwable throwable) {
        return ApiDataResponseVo.<T>builder()
                .success(false)
                .code(ErrorCodeEnum.UNKNOWN_FAIL.getCode())
                .error(ErrorResponseVo.of(throwable))
                .build();
    }
}