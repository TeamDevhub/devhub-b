package teamdevhub.devhub.adapter.in.common.vo;

import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ApiResponse", description = "표준 API 응답 구조")
public class ApiResponseVo <T> {

    @Schema(description = "성공 여부")
    private boolean success;
    @Schema(description = "응답 코드")
    private String code;
    @Schema(description = "단건 응답 데이터")
    private T data;
    @Schema(description = "리스트 응답 데이터")
    private List<T> dataList;
    @Schema(description = "페이징 정보")
    private PageVo pagination;
    @Schema(description = "에러 발생 시 상세 정보")
    private ErrorResponseVo error;


    public static <T> ApiResponseVo<T> successWithData(SuccessCodeEnum successCodeEnum, T data) {
        return ApiResponseVo.<T>builder()
                .success(true)
                .code(successCodeEnum.getCode())
                .data(data)
                .build();
    }

    public static ApiResponseVo<Void> successWithoutData(SuccessCodeEnum successCodeEnum) {
        return ApiResponseVo.<Void>builder()
                .success(true)
                .code(successCodeEnum.getCode())
                .build();
    }

    public static <T> ApiResponseVo<T> successWithDataList(SuccessCodeEnum successCodeEnum, List<T> dataList, PageVo pageVo) {
        return ApiResponseVo.<T>builder()
                .success(true)
                .code(successCodeEnum.getCode())
                .dataList(dataList)
                .pagination(pageVo)
                .build();
    }

    public static <T> ApiResponseVo<T> failureWithData(ErrorCodeEnum errorCodeEnum, T data) {
        return ApiResponseVo.<T>builder()
                .success(false)
                .code(errorCodeEnum.getCode())
                .data(data)
                .error(ErrorResponseVo.of(errorCodeEnum))
                .build();
    }
    
    public static <T> ApiResponseVo<T> failureWithoutData(ErrorCodeEnum errorCodeEnum) {
        return ApiResponseVo.<T>builder()
                .success(false)
                .code(errorCodeEnum.getCode())
                .error(ErrorResponseVo.of(errorCodeEnum))
                .build();
    }

    public static ApiResponseVo<?> failureWithMessage(ErrorCodeEnum errorCodeEnum, String message) {
        return ApiResponseVo.builder()
                .success(false)
                .code(errorCodeEnum.getCode())
                .error(new ErrorResponseVo(errorCodeEnum.getCode(), message))
                .build();
    }

    public static <T> ApiResponseVo<T> failureFromThrowable(Throwable throwable) {
        return ApiResponseVo.<T>builder()
                .success(false)
                .code(ErrorCodeEnum.UNKNOWN_FAIL.getCode())
                .error(ErrorResponseVo.of(throwable))
                .build();
    }

    public static <T> ApiResponseVo<T> failureFromFilter(Throwable throwable) {
        return ApiResponseVo.<T>builder()
                .success(false)
                .code(ErrorCodeEnum.UNKNOWN_FAIL.getCode())
                .error(ErrorResponseVo.of(throwable))
                .build();
    }
}