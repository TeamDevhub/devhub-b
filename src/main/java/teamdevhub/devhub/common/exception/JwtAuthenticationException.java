package teamdevhub.devhub.common.exception;

import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final ErrorCodeEnum errorCodeEnum;

    public JwtAuthenticationException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
    }

    public static JwtAuthenticationException of(ErrorCodeEnum errorCodeEnum) { return new JwtAuthenticationException(errorCodeEnum); }
}