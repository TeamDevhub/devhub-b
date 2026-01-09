package teamdevhub.devhub.adapter.out.exception;

import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCode;

@Getter
public class AdapterDataException extends RuntimeException {

    private final ErrorCode errorCode;

    private AdapterDataException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static AdapterDataException of(ErrorCode errorCode) {
        return new AdapterDataException(errorCode);
    }
}
