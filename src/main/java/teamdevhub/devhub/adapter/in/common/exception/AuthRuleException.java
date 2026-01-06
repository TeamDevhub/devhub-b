package teamdevhub.devhub.adapter.in.common.exception;

import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class AuthRuleException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    private AuthRuleException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
    }

    public static AuthRuleException of(ErrorCodeEnum errorCodeEnum) {
        return new AuthRuleException(errorCodeEnum);
    }
}