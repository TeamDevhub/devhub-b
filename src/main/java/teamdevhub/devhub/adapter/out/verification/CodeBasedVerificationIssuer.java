package teamdevhub.devhub.adapter.out.verification;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.common.provider.verification.VerificationCodeProvider;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.VerificationMessage;
import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.domain.verification.VerificationType;
import teamdevhub.devhub.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.service.verification.IssuedVerification;

@RequiredArgsConstructor
public class CodeBasedVerificationIssuer implements VerificationIssuer {

    private final VerificationCodeProvider verificationCodeProvider;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public boolean supports(VerificationTarget target) {
        return target.type() == VerificationType.EMAIL || target.type() == VerificationType.PHONE;
    }

    @Override
    public IssuedVerification issue(VerificationTarget target) {
        String code = verificationCodeProvider.generateVerificationCode();

        Verification verification = Verification.issue(
                target,
                code,
                dateTimeProvider.now().plusMinutes(5)
        );

        VerificationMessage verificationMessage =
                new VerificationMessage(code, dateTimeProvider.now().plusMinutes(5));

        return IssuedVerification.withMessage(
                verification,
                verificationMessage
        );
    }
}
