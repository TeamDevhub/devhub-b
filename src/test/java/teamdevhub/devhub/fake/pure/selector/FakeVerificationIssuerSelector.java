package teamdevhub.devhub.fake.pure.selector;

import teamdevhub.devhub.shared.exception.BusinessRuleException;
import teamdevhub.devhub.core.auth.application.selector.VerificationIssuerSelector;
import teamdevhub.devhub.core.auth.port.out.VerificationIssuer;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.application.service.vo.IssuedVerification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;

import java.util.List;

public class FakeVerificationIssuerSelector implements VerificationIssuerSelector {

    private final List<VerificationIssuer> issuerList;

    public FakeVerificationIssuerSelector(List<VerificationIssuer> issuerList) {
        this.issuerList = issuerList;
    }

    @Override
    public IssuedVerification issueVerification(VerificationTarget verificationTarget) {
        return issuerList.stream()
                .filter(issuer -> issuer.supports(verificationTarget))
                .findFirst()
                .orElseThrow(
                        () -> BusinessRuleException.of(ErrorCode.VERIFICATION_FAIL))
                .issue(verificationTarget);
    }
}
