package teamdevhub.devhub.core.auth.application.service.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.vo.IssuedVerification;
import teamdevhub.devhub.core.auth.domain.Verification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.notification.application.selector.NotificationSenderSelector;
import teamdevhub.devhub.core.auth.application.selector.VerificationIssuerSelector;
import teamdevhub.devhub.core.auth.port.in.command.ConfirmVerificationCommand;
import teamdevhub.devhub.core.auth.port.in.command.IssueVerificationCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.VerificationUseCase;
import teamdevhub.devhub.core.common.provider.TimeProvider;
import teamdevhub.devhub.core.auth.port.out.VerificationRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationService implements VerificationUseCase {

    private final TimeProvider timeProvider;
    private final VerificationIssuerSelector verificationIssuerSelector;
    private final NotificationSenderSelector notificationSenderSelector;
    private final VerificationRepository verificationRepository;

    @Override
    public void issueVerification(IssueVerificationCommand issueVerificationCommand) {
        IssuedVerification issuedVerification = verificationIssuerSelector.issueVerification(issueVerificationCommand.verificationTarget());
        verificationRepository.save(issuedVerification.verification());
        issuedVerification.getVerificationMessage()
                .ifPresent(verificationMessage -> notificationSenderSelector.sendVerification(issueVerificationCommand.verificationTarget(), verificationMessage));
    }

    @Override
    public void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        Verification verification = verificationRepository.findByVerificationTarget(confirmVerificationCommand.verificationTarget());
        verification.confirm(confirmVerificationCommand.code(), timeProvider.now());
        verificationRepository.save(verification);
    }

    @Override
    public void assertAllowed(VerificationTarget verificationTarget) {
        Verification verification = verificationRepository.findByVerificationTarget(verificationTarget);
        verification.assertValid(timeProvider.now());
    }

    @Override
    public void consume(VerificationTarget verificationTarget) {
        verificationRepository.deleteByVerificationTarget(verificationTarget);
    }
}
