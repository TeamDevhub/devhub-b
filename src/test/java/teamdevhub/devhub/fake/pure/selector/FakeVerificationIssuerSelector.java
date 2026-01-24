package teamdevhub.devhub.fake.pure.selector;

import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.application.selector.verification.VerificationIssuerSelector;
import teamdevhub.devhub.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.application.service.verification.vo.IssuedVerification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

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
