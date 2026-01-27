package teamdevhub.devhub.shared.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse of(String errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode)
                .message(message)
                .build();
    }

    public static ErrorResponse of(Throwable throwable) {
        String message = resolveMessage(throwable);
        return ErrorResponse.builder()
                .code(ErrorCode.UNKNOWN_FAIL.getCode())
                .message(message)
                .build();
    }

    private static String resolveMessage(Throwable throwable) {
        if (throwable == null) {
            return ErrorCode.UNKNOWN_FAIL.getMessage();
        }

        if (throwable.getMessage() == null || throwable.getMessage().isBlank()) {
            return ErrorCode.UNKNOWN_FAIL.getMessage();
        }

        return throwable.getMessage();
    }
}