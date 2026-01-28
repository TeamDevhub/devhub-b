package teamdevhub.devhub.core.auth.application.issuer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.core.common.provider.TimeProvider;
import teamdevhub.devhub.core.auth.port.out.verification.VerificationCodeProvider;
import teamdevhub.devhub.core.auth.domain.Verification;
import teamdevhub.devhub.core.auth.domain.vo.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.VerificationType;

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
