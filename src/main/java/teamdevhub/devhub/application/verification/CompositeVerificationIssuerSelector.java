package teamdevhub.devhub.application.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.application.verification.issuer.VerificationIssuer;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.IssuedVerification;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeVerificationIssuerSelector implements VerificationIssuerSelector {

    private final List<VerificationIssuer> issuerList;

    public IssuedVerification issueVerification(VerificationTarget verificationTarget) {
        return issuerList.stream()
                .filter(issuer -> issuer.supports(verificationTarget))
                .findFirst()
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.VERIFICATION_FAIL))
                .issue(verificationTarget);
    }
}