package teamdevhub.devhub.application.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.application.verification.issuer.VerificationIssuer;
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
                .orElseThrow(() -> new IllegalStateException("No VerificationIssuer for " + verificationTarget.verificationType()))
                .issue(verificationTarget);
    }
}