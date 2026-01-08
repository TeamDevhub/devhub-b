package teamdevhub.devhub.common.provider.password;

public interface PasswordPolicyProvider {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}