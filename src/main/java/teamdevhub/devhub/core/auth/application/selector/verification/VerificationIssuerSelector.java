package teamdevhub.devhub.core.auth.application.selector.verification;

import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;

public interface VerificationIssuerSelector {

    IssuedVerification issueVerification(VerificationTarget verificationTarget);
}
