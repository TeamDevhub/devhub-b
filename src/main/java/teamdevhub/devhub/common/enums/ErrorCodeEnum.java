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

    PROJECT_POSITION_RECRUITMENT_FAIL("ERR.DVH.0051", "모집 인원은 최소 1명 이상이어야 합니다.", BAD_REQUEST),
    PROJECT_PERIOD_FAIL("ERR.DVH.0052", "프로젝트 종료일은 시작일 이후여야 합니다.",BAD_REQUEST),
    PROJECT_TITLE_FAIL("ERR.DVH.0053", "프로젝트 제목은 공백일 수 없습니다.",BAD_REQUEST),
    PROJECT_DESCRIPTION_FAIL("ERR.DVH.0054", "프로젝트 설명은 100자를 넘길 수 없습니다.",BAD_REQUEST),
    PROJECT_PROGRESS_FAIL("ERR.DVH.0055", "프로젝트 진행 방식은 비어 있을 수 없습니다.",BAD_REQUEST),
    PROJECT_WRONG_PROGRESS_FAIL("ERR.DVH.0056", "잘못된 프로젝트 진행 방식입니다.",BAD_REQUEST),
    PROJECT_RECRUITMENT_FAIL("ERR.DVH.0057", "모집 유형은 비어 있을 수 없습니다.",BAD_REQUEST),
    PROJECT_WRONG_RECRUITMENT_FAIL("ERR.DVH.0058", "잘못된 모집 유형입니다.",BAD_REQUEST),

    APPLICATION_PROJECT_FAIL("ERR.DVH.0070", "프로젝트 식별값은 필수입니다.",BAD_REQUEST),
    APPLICATION_USER_FAIL("ERR.DVH.0071", "이메일은 필수입니다.",BAD_REQUEST),
    APPLICATION_APPLY_FAIL("ERR.DVH.0072", "이미 지원한 내역이 존재합니다.",BAD_REQUEST),
    APPLICATION_APPROVE_FAIL("ERR.DVH.0073", "승인할 수 없는 상태입니다.",BAD_REQUEST),
    APPLICATION_REJECT_FAIL("ERR.DVH.0074", "거절할 수 없는 상태입니다.",BAD_REQUEST),
    APPLICATION_CANCEL_FAIL("ERR.DVH.0075", "승인된 지원은 취소할 수 없습니다.",BAD_REQUEST),

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