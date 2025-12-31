package teamdevhub.devhub.adapter.in.common.vo;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;

@Getter
@Builder
public class ErrorResponseVo {

    private String code;
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