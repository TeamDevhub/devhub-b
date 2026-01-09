package teamdevhub.devhub.common.provider.verification;

import java.security.SecureRandom;

public class SystemEmailVerificationCodeProvider implements EmailVerificationCodeProvider {

    private final SecureRandom RANDOM = new SecureRandom();

    public String generateEmailVerificationCode() {
        int CODE_LENGTH = 6;
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(RANDOM.nextInt(10));
        }
        return code.toString();
    }
}