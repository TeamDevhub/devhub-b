package teamdevhub.devhub.port.out.common;

public interface PasswordPolicyPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}