package teamdevhub.devhub.domain.verification.vo;

import teamdevhub.devhub.domain.verification.Verification;

import java.util.Optional;

public record IssuedVerification(Verification verification, VerificationMessage verificationMessage) {

    public static IssuedVerification withVerificationMessage(Verification verification, VerificationMessage message) {
        return new IssuedVerification(verification, message);
    }

    public Optional<VerificationMessage> getVerificationMessage() {
        return Optional.ofNullable(verificationMessage);
    }
}