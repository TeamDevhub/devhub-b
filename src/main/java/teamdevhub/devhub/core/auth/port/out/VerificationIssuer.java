package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.application.service.vo.IssuedVerification;

public interface VerificationIssuer {

    boolean supports(VerificationTarget verificationTarget);
    IssuedVerification issue(VerificationTarget verificationTarget);
}
