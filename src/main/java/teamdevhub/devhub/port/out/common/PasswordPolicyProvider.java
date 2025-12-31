package teamdevhub.devhub.port.out.common;

public interface PasswordPolicyProvider {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}