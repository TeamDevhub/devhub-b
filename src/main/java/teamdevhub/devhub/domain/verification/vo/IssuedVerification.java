package teamdevhub.devhub.domain.verification.vo;

import teamdevhub.devhub.domain.verification.Verification;

import java.util.Optional;

public record IssuedVerification(Verification verification, VerificationMessage verificationMessage) {

    public static IssuedVerification withVerificationMessage(Verification verification, VerificationMessage verificationMessage) {
        return new IssuedVerification(verification, verificationMessage);
    }

    public Optional<VerificationMessage> getVerificationMessage() {
        return Optional.ofNullable(verificationMessage);
    }
}