package teamdevhub.devhub.common.provider.verification;

import teamdevhub.devhub.domain.verification.VerificationType;

public interface VerificationCodeProvider {
    boolean supports(VerificationType type);
    String generateVerificationCode();
}
