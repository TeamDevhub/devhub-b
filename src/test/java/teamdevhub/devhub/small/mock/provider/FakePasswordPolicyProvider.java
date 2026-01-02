package teamdevhub.devhub.small.mock.provider;

import teamdevhub.devhub.port.out.common.PasswordPolicyProvider;

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
