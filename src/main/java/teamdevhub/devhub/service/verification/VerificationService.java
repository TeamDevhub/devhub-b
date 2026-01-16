package teamdevhub.devhub.service.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.out.common.sender.CompositeMessageSender;
import teamdevhub.devhub.adapter.out.verification.CompositeVerificationIssuer;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.port.out.verification.VerificationRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final CompositeVerificationIssuer verificationIssuer;
    private final CompositeMessageSender messageSender;
    private final DateTimeProvider dateTimeProvider;

    public void issue(VerificationTarget target) {
        IssuedVerification issued = verificationIssuer.issue(target);

        verificationRepository.save(issued.verification());

        issued.message().ifPresent(message ->
                messageSender.sendVerification(target, message)
        );
    }

    public boolean verify(VerificationTarget target, String inputCode) {
        Verification verification = verificationRepository.findByTarget(target);

        boolean result = verification.verify(
                inputCode,
                dateTimeProvider.now()
        );

        verificationRepository.save(verification);
        return result;
    }

    public boolean isVerified(VerificationTarget target) {
        return verificationRepository
                .findByTarget(target)
                .isVerified();
    }
}
