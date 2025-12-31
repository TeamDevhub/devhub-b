package teamdevhub.devhub.adapter.in.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiResponse", description = "표준 API 응답 구조")
public class ApiDataResponseVo<T> {

    @Schema(description = "성공 여부")
    private boolean success;
    @Schema(description = "응답 코드")
    private String code;
    @Schema(description = "단건 응답 데이터")
    private T data;
    @Schema(description = "에러 발생 시 상세 정보")
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