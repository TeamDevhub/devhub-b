package teamdevhub.devhub.adapter.out.common.exception;

import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;

@Getter
public class DataAccessException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    private DataAccessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
    }

    public static DataAccessException of(ErrorCodeEnum errorCodeEnum) {
        return new DataAccessException(errorCodeEnum);
    }
}
