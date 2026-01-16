package teamdevhub.devhub.adapter.out.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.service.verification.IssuedVerification;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeVerificationIssuer {
    private final List<VerificationIssuer> issuers;

    public IssuedVerification issue(VerificationTarget target) {
        return issuers.stream()
                .filter(it -> it.supports(target))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No VerificationIssuer for " + target.type()
                        )
                )
                .issue(target);
    }
}