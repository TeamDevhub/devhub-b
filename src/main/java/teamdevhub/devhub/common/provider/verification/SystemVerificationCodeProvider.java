package teamdevhub.devhub.common.provider.verification;

import teamdevhub.devhub.domain.verification.vo.VerificationType;

import java.security.SecureRandom;

public class SystemVerificationCodeProvider implements VerificationCodeProvider {

    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public boolean supports(VerificationType type) {
        return type == VerificationType.EMAIL || type == VerificationType.PHONE;
    }

    @Override
    public String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(RANDOM.nextInt(10));
        }
        return code.toString();
    }
}