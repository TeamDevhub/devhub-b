package teamdevhub.devhub.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCodeEnum {

    VALIDATION_FAIL("ERR.DVH.000", "검증 오류", BAD_REQUEST),

    READ_FAIL("ERR.DVH.0001", "조회 오류", INTERNAL_SERVER_ERROR),
    CREATE_FAIL("ERR.DVH.0002", "작성 오류",INTERNAL_SERVER_ERROR),
    UPDATE_FAIL("ERR.DVH.0003", "수정 오류",INTERNAL_SERVER_ERROR),
    DELETE_FAIL("ERR.DVH.0004", "삭제 오류",INTERNAL_SERVER_ERROR),

    TOKEN_EXPIRED("ERR.DVH.0010", "토큰이 만료되었습니다.", BAD_REQUEST),
    TOKEN_INVALID("ERR.DVH.0011", "유효하지 않은 토큰입니다.", BAD_REQUEST),
    TOKEN_UNSUPPORTED("ERR.DVH.0012", "지원되지 않는 토큰 형식입니다.", BAD_REQUEST),
    MISSING_AUTH_HEADER("ERR.DVH.0013", "Authorization Header가 없습니다.", BAD_REQUEST),
    USER_NOT_FOUND("ERR.DVH.0014", "로그인된 사용자가 존재하지 않습니다.", UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("ERR.DVH.0015", "유효하지 않은 Refresh Token 입니다.", UNAUTHORIZED),
    REFRESH_TOKEN_MISMATCH("ERR.DVH.0016", "스토리지에 저장된 Token과 일치하지 않습니다.", UNAUTHORIZED),
    AUTH_INVALID("ERR.DVH.0017", "유효하지 않은 인증 정보입니다.", UNAUTHORIZED),
    AUTH_FAIL("ERR.DVH.0018", "인증에 실패했습니다.", UNAUTHORIZED),
    SIGNUP_FAIL("ERR.DVH.0019", "회원가입에 실패했습니다.", BAD_REQUEST),
    LOGIN_FAIL("ERR.DVH.0020", "로그인에 실패했습니다.", BAD_REQUEST),
    DUPLICATE_EMAIL("ERR.DVH.0021", "중복된 이메일을 사용할 수는 없습니다.", BAD_REQUEST),
    EMAIL_CERTIFICATION_CODE_ALREADY_SENT("ERR.DVH.0022", "인증 코드가 이미 발송되었습니다. 잠시 후 재시도해주세요.", BAD_REQUEST),
    EMAIL_NOT_CONFIRMED("ERR.DVH.0023", "E-mail 인증에 실패했습니다.", BAD_REQUEST),
    EMAIL_CERTIFICATION_CODE_INVALID("ERR.DVH.0024", "E-mail 인증코드가 일치하지 않습니다.", BAD_REQUEST),

    USER_ID_FAIL("ERR.DVH.0030", "사용자 ID 값이 잘못되었습니다.",BAD_REQUEST),
    USER_PASSWORD_FAIL("ERR.DVH.0031", "사용자 비밀번호 값이 잘못되었습니다.",BAD_REQUEST),
    ALREADY_DELETED("ERR.DVH.0032", "이미 탈퇴한 회원입니다.", BAD_REQUEST ),
    USER_POSITION_REQUIRED("ERR.DVH.0033", "관심 포지션은 필수입니다.", BAD_REQUEST),
    USER_SKILL_REQUIRED("ERR.DVH.0034", "보유 스킬목록은 필수입니다.", BAD_REQUEST),

    EMAIL_SEND_FAIL("ERR.DVH.0050", "이메일 발송이 실패했습니다.",INTERNAL_SERVER_ERROR),

    UNKNOWN_FAIL("ERR.DVH.9999", "원인 미상의 에러가 발생했습니다.",BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCodeEnum(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}