package teamdevhub.devhub.application.selector.verification.issuer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.port.out.provider.TimeProvider;
import teamdevhub.devhub.port.out.provider.VerificationCodeProvider;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.domain.verification.vo.IssuedVerification;

@Component
@RequiredArgsConstructor
public class EmailVerificationIssuer implements VerificationIssuer {

    private final VerificationCodeProvider verificationCodeProvider;
    private final TimeProvider timeProvider;

    @Override
    public boolean supports(VerificationTarget verificationTarget) {
        return verificationTarget.verificationType() == VerificationType.EMAIL;
    }

    @Override
    public IssuedVerification issue(VerificationTarget verificationTarget) {
        String verificationCode = verificationCodeProvider.generateVerificationCode();
        VerificationMessage verificationMessage = new VerificationMessage(verificationCode, timeProvider.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        return IssuedVerification.withVerificationMessage(verification, verificationMessage);
    }
}
