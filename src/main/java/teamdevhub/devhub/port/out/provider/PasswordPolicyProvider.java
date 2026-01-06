package teamdevhub.devhub.port.out.provider;

public interface PasswordPolicyProvider {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}