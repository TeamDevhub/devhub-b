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
@AllArgsConstructor
@NoArgsConstructor
public class DataApiResponseDto<T> {

    private boolean success;
    private String code;
    private T data;
    private ErrorResponseVo error;

    public static <T> DataApiResponseDto<T> successWithData(SuccessCode successCode, T data) {
        return DataApiResponseDto.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .data(data)
                .build();
    }

    public static DataApiResponseDto<Void> successWithoutData(SuccessCode successCode) {
        return DataApiResponseDto.<Void>builder()
                .success(true)
                .code(successCode.getCode())
                .build();
    }
    
    public static <T> DataApiResponseDto<T> failureWithoutData(ErrorCode errorCode) {
        return DataApiResponseDto.<T>builder()
                .success(false)
                .code(errorCode.getCode())
                .error(ErrorResponseVo.of(errorCode))
                .build();
    }

    public static DataApiResponseDto<?> failureWithMessage(ErrorCode errorCode, String message) {
        return DataApiResponseDto.builder()
                .success(false)
                .code(errorCode.getCode())
                .error(ErrorResponseVo.of(errorCode.getCode(), message))
                .build();
    }

    public static <T> DataApiResponseDto<T> failureFromThrowable(Throwable throwable) {
        return DataApiResponseDto.<T>builder()
                .success(false)
                .code(ErrorCode.UNKNOWN_FAIL.getCode())
                .error(ErrorResponseVo.of(throwable))
                .build();
    }
}