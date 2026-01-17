package teamdevhub.devhub.adapter.out.provider.password;

import org.springframework.security.crypto.password.PasswordEncoder;
import teamdevhub.devhub.port.out.provider.PasswordPolicyProvider;

public class BCryptPasswordPolicyProvider implements PasswordPolicyProvider {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordPolicyProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
