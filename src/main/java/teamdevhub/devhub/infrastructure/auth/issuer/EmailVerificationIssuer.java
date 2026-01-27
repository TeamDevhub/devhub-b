package teamdevhub.devhub.infrastructure.auth.issuer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.port.out.VerificationIssuer;
import teamdevhub.devhub.core.auth.application.service.vo.IssuedVerification;
import teamdevhub.devhub.core.common.provider.TimeProvider;
import teamdevhub.devhub.core.common.provider.VerificationCodeProvider;
import teamdevhub.devhub.core.auth.domain.Verification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationType;

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
