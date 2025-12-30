package teamdevhub.devhub.common.exception;

import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class DomainRuleException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    private DomainRuleException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
    }

    public static DomainRuleException of(ErrorCodeEnum errorCodeEnum) {
        return new DomainRuleException(errorCodeEnum);
    }
}