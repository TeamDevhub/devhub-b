package teamdevhub.devhub.common.exception;

import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;

@Getter
public class FilterRuleException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    private FilterRuleException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
    }

    public static FilterRuleException of(ErrorCodeEnum errorCodeEnum) {
        return new FilterRuleException(errorCodeEnum);
    }
}
