package teamdevhub.devhub.common.enums;

import lombok.Getter;

@Getter
public enum SuccessCodeEnum {

    READ_SUCCESS("SUC.DVH.0001", "조회 성공"),
    CREATE_SUCCESS("SUC.DVH.0002", "생성 성공"),
    UPDATE_SUCCESS("SUC.DVH.0003", "수정 성공"),
    DELETE_SUCCESS("SUC.DVH.0004", "삭제 성공"),

    SIGNUP_SUCCESS("SUC.DVH.0010", "회원가입 성공"),
    LOGIN_SUCCESS("SUC.DVH.0011", "로그인 성공"),
    LOGOUT_SUCCESS("SUC.DVH.0012", "로그아웃 성공"),
    EMAIL_CERTIFICATION_SENT("SUC.DVH.0013", "인증코드 발송 성공"),
    EMAIL_CERTIFICATION_SUCCESS("SUC.DVH.0014", "인증코드 검증 성공"),

    USER_DELETE_SUCCESS("SUC.DVH.0015", "회원탈퇴 성공");

    private final String code;
    private final String message;

    SuccessCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}