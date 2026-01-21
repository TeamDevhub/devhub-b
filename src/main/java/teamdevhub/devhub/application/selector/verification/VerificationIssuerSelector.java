package teamdevhub.devhub.application.selector.verification;

import teamdevhub.devhub.domain.verification.vo.IssuedVerification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

public interface VerificationIssuerSelector {

    IssuedVerification issueVerification(VerificationTarget verificationTarget);
}
