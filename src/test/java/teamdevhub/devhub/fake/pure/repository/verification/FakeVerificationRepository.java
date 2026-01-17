package teamdevhub.devhub.fake.pure.repository.verification;

import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.out.verification.VerificationRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeVerificationRepository implements VerificationRepository {

    private final Map<String, Verification> store = new HashMap<>();
    private long sequence = 1L;

    @Override
    public void save(Verification verification) {
        if (verification.getId() == null) {
            Verification savedVerification = Verification.of(
                    sequence++,
                    verification.getVerificationTarget(),
                    verification.getCode(),
                    verification.getExpiredAt(),
                    verification.isVerified()
            );
            store.put(savedVerification.getVerificationTarget().value(), savedVerification);
        } else {
            store.put(verification.getVerificationTarget().value(), verification);
        }
    }

    @Override
    public Verification findByVerificationTarget(VerificationTarget verificationTarget) {
        Verification verification = store.get(verificationTarget.value());
        if (verification == null) {
            throw BusinessRuleException.of(ErrorCode.VERIFICATION_NOT_EXISTED);
        }
        return verification;
    }

    @Override
    public void deleteByVerificationTarget(VerificationTarget verificationTarget) {
        store.remove(verificationTarget.value());
    }
}
