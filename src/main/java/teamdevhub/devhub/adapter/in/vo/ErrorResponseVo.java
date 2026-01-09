package teamdevhub.devhub.adapter.in.vo;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCode;

@Getter
@Builder
public class ErrorResponseVo {

    private String code;

    private String message;

    public static ErrorResponseVo of(ErrorCode errorCode) {
        return ErrorResponseVo.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponseVo of(String errorCode, String message) {
        return ErrorResponseVo.builder()
                .code(errorCode)
                .message(message)
                .build();
    }

    public static ErrorResponseVo of(Throwable throwable) {
        String message = resolveMessage(throwable);
        return ErrorResponseVo.builder()
                .code(ErrorCode.UNKNOWN_FAIL.getCode())
                .message(message)
                .build();
    }

    private static String resolveMessage(Throwable throwable) {
        if (throwable == null) {
            return "Unexpected system error occurred";
        }

        if (throwable.getMessage() == null || throwable.getMessage().isBlank()) {
            return "Unexpected system error occurred";
        }

        return throwable.getMessage();
    }
}