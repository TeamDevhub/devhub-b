package teamdevhub.devhub.port.out.verification;

import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.service.verification.IssuedVerification;

public interface VerificationIssuer {
    boolean supports(VerificationTarget target);
    IssuedVerification issue(VerificationTarget verificationTarget);
}
