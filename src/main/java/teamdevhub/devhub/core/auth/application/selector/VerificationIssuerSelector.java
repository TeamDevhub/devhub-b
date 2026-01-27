package teamdevhub.devhub.core.auth.application.selector;

import teamdevhub.devhub.core.auth.application.service.vo.IssuedVerification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;

public interface VerificationIssuerSelector {

    IssuedVerification issueVerification(VerificationTarget verificationTarget);
}
