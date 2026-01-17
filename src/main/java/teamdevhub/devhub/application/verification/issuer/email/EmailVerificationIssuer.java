package teamdevhub.devhub.application.verification.issuer.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.application.verification.issuer.VerificationIssuer;
import teamdevhub.devhub.port.out.provider.DateTimeProvider;
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
    private final DateTimeProvider dateTimeProvider;

    @Override
    public boolean supports(VerificationTarget verificationTarget) {
        return verificationTarget.verificationType() == VerificationType.EMAIL || verificationTarget.verificationType() == VerificationType.PHONE;
    }

    @Override
    public IssuedVerification issue(VerificationTarget target) {
        String verificationCode = verificationCodeProvider.generateVerificationCode();

        Verification verification = Verification.issue(target, verificationCode, dateTimeProvider.now().plusMinutes(5));
        VerificationMessage verificationMessage = new VerificationMessage(verificationCode, dateTimeProvider.now().plusMinutes(5));

        return IssuedVerification.withVerificationMessage(verification, verificationMessage);
    }
}
