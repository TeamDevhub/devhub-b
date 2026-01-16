package teamdevhub.devhub.fake.pure.provider;

import teamdevhub.devhub.port.out.authentication.PasswordPolicyProvider;

public class FakePasswordPolicyProvider implements PasswordPolicyProvider {

    private static final String PREFIX = "ENC.";

    @Override
    public String encode(String rawPassword) {
        return PREFIX + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return hashedPassword.equals(PREFIX + rawPassword);
    }
}
