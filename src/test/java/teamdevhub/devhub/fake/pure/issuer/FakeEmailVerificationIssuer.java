package teamdevhub.devhub.fake.pure.issuer;

import teamdevhub.devhub.core.auth.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.core.auth.domain.Verification;
import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.core.common.provider.TimeProvider;

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
