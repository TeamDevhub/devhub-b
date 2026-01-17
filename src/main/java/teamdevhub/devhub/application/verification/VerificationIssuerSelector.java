package teamdevhub.devhub.application.verification;

import teamdevhub.devhub.domain.verification.vo.IssuedVerification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

public interface VerificationIssuerSelector {
    IssuedVerification issueVerification(VerificationTarget verificationTarget);
}
