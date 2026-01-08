package teamdevhub.devhub.common.enums;

public enum EmailType {

    SIGNUP_CERTIFICATION("[DEVHUB] 회원가입 이메일 인증 코드"),
    PROJECT_PROGRESSION_END_NOTIFICATION("[DEVHUB] 참여 프로젝트 진행 종료 안내");

    private final String subject;

    EmailType(String subject) {
        this.subject = subject;
    }

    public String subject() {
        return subject;
    }
}
