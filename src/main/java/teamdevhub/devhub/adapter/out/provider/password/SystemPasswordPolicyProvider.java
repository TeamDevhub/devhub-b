package teamdevhub.devhub.adapter.out.provider.password;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.out.provider.PasswordPolicyProvider;

@Component
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
