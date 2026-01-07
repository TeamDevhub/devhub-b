package teamdevhub.devhub.common.exception;

import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCode;

@Getter
public class FilterRuleException extends RuntimeException {

    private final ErrorCode errorCode;

    private FilterRuleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static FilterRuleException of(ErrorCode errorCode) {
        return new FilterRuleException(errorCode);
    }
}
