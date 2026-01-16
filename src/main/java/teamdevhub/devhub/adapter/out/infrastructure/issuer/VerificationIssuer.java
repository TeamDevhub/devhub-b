package teamdevhub.devhub.adapter.out.infrastructure.issuer;

import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.IssuedVerification;

public interface VerificationIssuer {
    boolean supports(VerificationTarget verificationTarget);
    IssuedVerification issue(VerificationTarget verificationTarget);
}
