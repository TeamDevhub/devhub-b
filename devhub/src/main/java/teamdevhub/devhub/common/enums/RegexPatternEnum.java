package teamdevhub.devhub.common.enums;

public enum RegexPatternEnum {

    SIGNUP_USERNAME("^[a-zA-Z0-9_]{5,20}$","5~20자, 영문 대/소문자, 숫자를 조합하여 생성할 수 있습니다."),
    SIGNUP_PASSWORD("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_\\-+=<>?{}[\\]~])[A-Za-z\\d!@#$%^&*()_\\-+=<>?{}[\\]~]{8,20}$\n","최소 8자~최대 20자, 특수문자 1개 이상, 숫자 1개 이상, 영문 소문자 1개 이상, 영문 대문자 1개 이상 포함해야 합니다."),
    USERNAME("^[a-zA-Z0-9가-힣_]{1,15}$", "사용자명은 한글,영문,숫자,_만 사용 가능하며 최대 15자까지 가능합니다.");

    private final String regexp;
    private final String message;

    RegexPatternEnum(String regexp, String message) {
        this.regexp = regexp;
        this.message = message;
    }

    public String regexp() {
        return regexp;
    }

    public String message() {
        return message;
    }
}