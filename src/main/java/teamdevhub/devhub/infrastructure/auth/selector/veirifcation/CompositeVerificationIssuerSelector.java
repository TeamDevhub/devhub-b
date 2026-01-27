package teamdevhub.devhub.infrastructure.auth.selector.veirifcation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.application.selector.VerificationIssuerSelector;
import teamdevhub.devhub.shared.exception.BusinessRuleException;
import teamdevhub.devhub.core.auth.port.out.VerificationIssuer;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.application.service.vo.IssuedVerification;

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