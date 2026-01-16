package teamdevhub.devhub.port.out.verification;

import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.VerificationTarget;

public interface VerificationRepository {

    Verification findByTarget(VerificationTarget target);

    void save(Verification verification);

    void deleteByTarget(VerificationTarget target);
}
