package teamdevhub.devhub.shared.web.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.SuccessCode;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataApiResponse<T> {

    @JsonProperty("isSuccess")
    private boolean isSuccess;
    private String code;
    private T data;
    private ErrorResponse error;

    public static <T> DataApiResponse<T> successWithData(SuccessCode successCode, T data) {
        return DataApiResponse.<T>builder()
                .isSuccess(true)
                .code(successCode.getCode())
                .data(data)
                .build();
    }

    public static DataApiResponse<Void> successWithoutData(SuccessCode successCode) {
        return DataApiResponse.<Void>builder()
                .isSuccess(true)
                .code(successCode.getCode())
                .build();
    }
    
    public static <T> DataApiResponse<T> failureWithoutData(ErrorCode errorCode) {
        return DataApiResponse.<T>builder()
                .isSuccess(false)
                .code(errorCode.getCode())
                .error(ErrorResponse.of(errorCode))
                .build();
    }

    public static DataApiResponse<?> failureWithMessage(ErrorCode errorCode, String message) {
        return DataApiResponse.builder()
                .isSuccess(false)
                .code(errorCode.getCode())
                .error(ErrorResponse.of(errorCode.getCode(), message))
                .build();
    }

    public static <T> DataApiResponse<T> failureFromThrowable(Throwable throwable) {
        return DataApiResponse.<T>builder()
                .isSuccess(false)
                .code(ErrorCode.UNKNOWN_FAIL.getCode())
                .error(ErrorResponse.of(throwable))
                .build();
    }
}