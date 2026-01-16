package teamdevhub.devhub.port.out.verification;

import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

public interface VerificationRepository {

    Verification findByVerificationTarget(VerificationTarget verificationTarget);
    void save(Verification verification);
    void deleteByVerificationTarget(VerificationTarget verificationTarget);
}
