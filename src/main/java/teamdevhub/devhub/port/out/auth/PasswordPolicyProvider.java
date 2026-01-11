package teamdevhub.devhub.port.out.auth;

public interface PasswordPolicyProvider {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}