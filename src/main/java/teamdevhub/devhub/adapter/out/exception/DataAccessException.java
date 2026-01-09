package teamdevhub.devhub.adapter.out.exception;

import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCode;

@Getter
public class DataAccessException extends RuntimeException {

    private final ErrorCode errorCode;

    private DataAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static DataAccessException of(ErrorCode errorCode) {
        return new DataAccessException(errorCode);
    }
}
