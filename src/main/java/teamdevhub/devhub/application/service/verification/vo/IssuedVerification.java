package teamdevhub.devhub.application.service.verification.vo;

import lombok.Builder;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;

import java.util.Optional;

@Builder
public record IssuedVerification(Verification verification, VerificationMessage verificationMessage) {

    public static IssuedVerification withVerificationMessage(Verification verification, VerificationMessage verificationMessage) {
        return new IssuedVerification(verification, verificationMessage);
    }

    public Optional<VerificationMessage> getVerificationMessage() {
        return Optional.ofNullable(verificationMessage);
    }
}