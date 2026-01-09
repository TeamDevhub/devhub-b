package teamdevhub.devhub.common.provider.password;

import org.springframework.security.crypto.password.PasswordEncoder;

public class SystemPasswordPolicyProvider implements PasswordPolicyProvider {

    private final PasswordEncoder passwordEncoder;

    public SystemPasswordPolicyProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
