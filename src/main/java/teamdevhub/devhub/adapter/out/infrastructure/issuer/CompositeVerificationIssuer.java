package teamdevhub.devhub.adapter.out.infrastructure.issuer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.IssuedVerification;
import teamdevhub.devhub.port.out.verification.VerificationManager;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeVerificationIssuer implements VerificationManager {

    private final List<VerificationIssuer> issuerList;

    public IssuedVerification issueVerification(VerificationTarget verificationTarget) {
        return issuerList.stream()
                .filter(issuer -> issuer.supports(verificationTarget))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No VerificationIssuer for " + verificationTarget.type()))
                .issue(verificationTarget);
    }
}