package teamdevhub.devhub.port.out.verification;

import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.application.service.verification.vo.IssuedVerification;

public interface VerificationIssuer {

    boolean supports(VerificationTarget verificationTarget);
    IssuedVerification issue(VerificationTarget verificationTarget);
}
