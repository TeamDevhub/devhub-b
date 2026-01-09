package teamdevhub.devhub.adapter.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.adapter.in.vo.ErrorResponseVo;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.SuccessCode;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataResponseDto<T> {

    private boolean success;

    private String code;

    private T data;

    private ErrorResponseVo error;

    public static <T> ApiDataResponseDto<T> successWithData(SuccessCode successCode, T data) {
        return ApiDataResponseDto.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .data(data)
                .build();
    }

    public static ApiDataResponseDto<Void> successWithoutData(SuccessCode successCode) {
        return ApiDataResponseDto.<Void>builder()
                .success(true)
                .code(successCode.getCode())
                .build();
    }
    
    public static <T> ApiDataResponseDto<T> failureWithoutData(ErrorCode errorCode) {
        return ApiDataResponseDto.<T>builder()
                .success(false)
                .code(errorCode.getCode())
                .error(ErrorResponseVo.of(errorCode))
                .build();
    }

    public static ApiDataResponseDto<?> failureWithMessage(ErrorCode errorCode, String message) {
        return ApiDataResponseDto.builder()
                .success(false)
                .code(errorCode.getCode())
                .error(ErrorResponseVo.of(errorCode.getCode(), message))
                .build();
    }

    public static <T> ApiDataResponseDto<T> failureFromThrowable(Throwable throwable) {
        return ApiDataResponseDto.<T>builder()
                .success(false)
                .code(ErrorCode.UNKNOWN_FAIL.getCode())
                .error(ErrorResponseVo.of(throwable))
                .build();
    }
}