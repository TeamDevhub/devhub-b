package teamdevhub.devhub.adapter.in.common.vo;

import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "에러 상세 정보")
public class ErrorResponseVo {

    @Schema(description = "오류 코드")
    private String code;
    @Schema(description = "오류 메시지")
    private String message;

    public static ErrorResponseVo of(ErrorCodeEnum errorCode) {
        return ErrorResponseVo.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponseVo of(Throwable throwable) {
        return ErrorResponseVo.builder()
                .code(ErrorCodeEnum.UNKNOWN_FAIL.getCode())
                .message(throwable.getMessage() != null ? throwable.getMessage() : "Unexpected system error occurred")
                .build();
    }
}