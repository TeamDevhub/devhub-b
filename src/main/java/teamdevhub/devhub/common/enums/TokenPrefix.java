package teamdevhub.devhub.common.enums;

public enum TokenPrefix {
    BEARER("Bearer ");
    private final String value;
    TokenPrefix(String value) { this.value = value; }
    public String value() { return value; }
}
