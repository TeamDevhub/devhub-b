package teamdevhub.devhub.core.auth.port.out.verification;

import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;
import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;

public interface VerificationIssuer {

    boolean supports(VerificationTarget verificationTarget);
    IssuedVerification issue(VerificationTarget verificationTarget);
}
