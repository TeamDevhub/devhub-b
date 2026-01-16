package teamdevhub.devhub.adapter.out.infrastructure.issuer.email;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.common.provider.verification.VerificationCodeProvider;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.adapter.out.infrastructure.issuer.VerificationIssuer;
import teamdevhub.devhub.domain.verification.vo.IssuedVerification;

@RequiredArgsConstructor
public class EmailVerificationIssuer implements VerificationIssuer {

    private final VerificationCodeProvider verificationCodeProvider;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public boolean supports(VerificationTarget verificationTarget) {
        return verificationTarget.type() == VerificationType.EMAIL || verificationTarget.type() == VerificationType.PHONE;
    }

    @Override
    public IssuedVerification issue(VerificationTarget target) {
        String verificationCode = verificationCodeProvider.generateVerificationCode();

        Verification verification = Verification.issue(target, verificationCode, dateTimeProvider.now().plusMinutes(5));
        VerificationMessage verificationMessage = new VerificationMessage(verificationCode, dateTimeProvider.now().plusMinutes(5));

        return IssuedVerification.withVerificationMessage(verification, verificationMessage);
    }
}
