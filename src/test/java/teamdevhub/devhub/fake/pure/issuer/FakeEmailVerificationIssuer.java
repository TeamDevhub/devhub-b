package teamdevhub.devhub.fake.pure.issuer;

import teamdevhub.devhub.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.IssuedVerification;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.port.out.provider.TimeProvider;

public class FakeEmailVerificationIssuer implements VerificationIssuer {

    private final VerificationType verificationType;
    private final TimeProvider timeProvider;
    private final String fixedCode;

    public FakeEmailVerificationIssuer(
            VerificationType supportedType,
            String fixedCode,
            TimeProvider timeProvider
    ) {
        this.verificationType = supportedType;
        this.fixedCode = fixedCode;
        this.timeProvider = timeProvider;
    }

    @Override
    public boolean supports(VerificationTarget verificationTarget) {
        return verificationTarget.verificationType() == verificationType;
    }

    @Override
    public IssuedVerification issue(VerificationTarget verificationTarget) {
        VerificationMessage verificationMessage = new VerificationMessage(fixedCode, timeProvider.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        return IssuedVerification.withVerificationMessage(verification, verificationMessage);
    }
}
