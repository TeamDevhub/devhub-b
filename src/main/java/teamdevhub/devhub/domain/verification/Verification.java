package teamdevhub.devhub.domain.verification;

import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

import java.time.LocalDateTime;
import java.util.Objects;

public class Verification {

    private final VerificationTarget verificationTarget;
    private final String code;
    private final LocalDateTime expiredAt;
    private boolean verified;

    private Verification(
            VerificationTarget verificationTarget,
            String code,
            LocalDateTime expiredAt,
            boolean verified
    ) {
        this.verificationTarget = Objects.requireNonNull(verificationTarget);
        this.code = code;
        this.expiredAt = expiredAt;
        this.verified = verified;
    }

    public static Verification issue(
            VerificationTarget target,
            String code,
            LocalDateTime expiredAt
    ) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(expiredAt);
        return new Verification(target, code, expiredAt, false);
    }

    public static Verification confirmed(
            VerificationTarget target
    ) {
        return new Verification(target, null, null, true);
    }

    public static Verification restore(
            VerificationTarget target,
            String code,
            LocalDateTime expiredAt,
            boolean verified
    ) {
        return new Verification(target, code, expiredAt, verified);
    }

    public boolean verify(String inputCode, LocalDateTime now) {
        if (verified) return true;
        if (expiredAt.isBefore(now)) return false;
        if (!Objects.equals(code, inputCode)) return false;
        this.verified = true;
        return true;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }

    public VerificationTarget getVerificationTarget() {
        return verificationTarget;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
