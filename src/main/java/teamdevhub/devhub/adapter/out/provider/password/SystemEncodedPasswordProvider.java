package teamdevhub.devhub.adapter.out.provider.password;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.out.provider.EncodedPasswordProvider;

@Component
public class SystemEncodedPasswordProvider implements EncodedPasswordProvider {

    private final PasswordEncoder passwordEncoder;

    public SystemEncodedPasswordProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
