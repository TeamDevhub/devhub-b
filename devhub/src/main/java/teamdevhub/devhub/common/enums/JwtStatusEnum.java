package teamdevhub.devhub.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum JwtStatusEnum {

    VALID("유효한 토큰입니다.", OK),
    INVALID("유효하지않은 토큰입니다.", FORBIDDEN),
    EXPIRED("토큰이 만료되었습니다.", UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;

    JwtStatusEnum(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}