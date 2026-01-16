package teamdevhub.devhub.domain.verification.vo;

import lombok.AllArgsConstructor;
import teamdevhub.devhub.domain.verification.Verification;

import java.util.Optional;

@AllArgsConstructor
public final class IssuedVerification {

    private final Verification verification;
    private final VerificationMessage verificationMessage;

    public static IssuedVerification withVerificationMessage(Verification verification, VerificationMessage message) {
        return new IssuedVerification(verification, message);
    }

    public static IssuedVerification withoutVerificationMessage(Verification verification) {
        return new IssuedVerification(verification, null);
    }

    public Verification getVerification() {
        return verification;
    }

    public Optional<VerificationMessage> getVerificationMessage() {
        return Optional.ofNullable(verificationMessage);
    }
}