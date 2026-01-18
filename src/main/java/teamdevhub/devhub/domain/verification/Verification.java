package teamdevhub.devhub.domain.verification;

import lombok.Getter;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Verification {

    private final Long id;
    private final VerificationTarget verificationTarget;
    private final String code;
    private final LocalDateTime expiredAt;
    private boolean verified;

    private Verification(
            Long id,
            VerificationTarget verificationTarget,
            String code,
            LocalDateTime expiredAt,
            boolean verified
    ) {
        this.id = id;
        this.verificationTarget = Objects.requireNonNull(verificationTarget);
        this.code = code;
        this.expiredAt = expiredAt;
        this.verified = verified;
    }

    public static Verification issue(VerificationTarget target, String code, LocalDateTime expiredAt) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(expiredAt);
        return new Verification(null, target, code, expiredAt, false);
    }

    public static Verification of(
            Long id,
            VerificationTarget target,
            String code,
            LocalDateTime expiredAt,
            boolean verified
    ) {
        return new Verification(id, target, code, expiredAt, verified);
    }

    public void confirm(String code, LocalDateTime now) {
        boolean success = verify(code, now);
        if (!success) {
            throw DomainRuleException.of(ErrorCode.VERIFICATION_FAIL);
        }
    }

    public void assertValid(LocalDateTime now) {
        if (!isVerified() || isExpired(now)) {
            throw DomainRuleException.of(ErrorCode.VERIFICATION_FAIL);
        }
    }

    private boolean verify(String inputCode, LocalDateTime now) {
        if (verified) return true;
        if (expiredAt.isBefore(now)) return false;
        if (!Objects.equals(code, inputCode)) return false;
        this.verified = true;
        return true;
    }

    private boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }
}
