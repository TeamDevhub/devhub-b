package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.Verification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;

public interface VerificationRepository {

    void save(Verification verification);
    Verification findByVerificationTarget(VerificationTarget verificationTarget);
    void deleteByVerificationTarget(VerificationTarget verificationTarget);
}
