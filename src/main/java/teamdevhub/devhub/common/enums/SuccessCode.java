package teamdevhub.devhub.common.enums;

import lombok.Getter;

@Getter
public enum SuccessCode {

    READ_SUCCESS("SUC.DVH.0001", "조회 성공했습니다."),
    CREATE_SUCCESS("SUC.DVH.0002", "생성 성공했습니다."),
    UPDATE_SUCCESS("SUC.DVH.0003", "수정 성공했습니다."),
    DELETE_SUCCESS("SUC.DVH.0004", "삭제 성공했습니다."),

    VERIFICATION_SENT("SUC.DVH.0010", "인증코드 발송이 성공했습니다."),
    VERIFICATION_SUCCESS("SUC.DVH.0011", "인증코드 검증이 성공했습니다."),

    SIGNUP_SUCCESS("SUC.DVH.0012", "회원가입이 성공했습니다."),
    LOGIN_SUCCESS("SUC.DVH.0013", "로그인이 성공했습니다."),
    LOGOUT_SUCCESS("SUC.DVH.0014", "로그아웃이 성공했습니다."),
    USER_DELETE_SUCCESS("SUC.DVH.0015", "회원탈퇴를 성공했습니다.");

    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}