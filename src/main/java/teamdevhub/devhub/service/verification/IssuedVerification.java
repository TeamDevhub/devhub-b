package teamdevhub.devhub.service.verification;

import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.VerificationMessage;

import java.util.Optional;

public final class IssuedVerification {

    private final Verification verification;
    private final VerificationMessage message;

    private IssuedVerification(
            Verification verification,
            VerificationMessage message
    ) {
        this.verification = verification;
        this.message = message;
    }

    public static IssuedVerification withMessage(
            Verification verification,
            VerificationMessage message
    ) {
        return new IssuedVerification(verification, message);
    }

    public static IssuedVerification withoutMessage(
            Verification verification
    ) {
        return new IssuedVerification(verification, null);
    }

    public Verification verification() {
        return verification;
    }

    public Optional<VerificationMessage> message() {
        return Optional.ofNullable(message);
    }
}