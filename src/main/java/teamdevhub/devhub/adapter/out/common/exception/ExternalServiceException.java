package teamdevhub.devhub.adapter.out.common.exception;

import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;

@Getter
public class ExternalServiceException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    private ExternalServiceException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
    }

    public static ExternalServiceException of(ErrorCodeEnum errorCodeEnum) {
        return new ExternalServiceException(errorCodeEnum);
    }
}
