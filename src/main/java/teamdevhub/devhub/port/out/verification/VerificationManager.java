package teamdevhub.devhub.port.out.verification;

import teamdevhub.devhub.domain.verification.vo.IssuedVerification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

public interface VerificationManager {
    IssuedVerification issueVerification(VerificationTarget verificationTarget);
}
