package teamdevhub.devhub.port.out.authentication;

public interface PasswordPolicyProvider {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}