package teamdevhub.devhub.common.exception;

import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    private BusinessRuleException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
    }

    public static BusinessRuleException of(ErrorCodeEnum errorCodeEnum) {
        return new BusinessRuleException(errorCodeEnum);
    }
}