package teamdevhub.devhub.application.service.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.application.selector.notification.NotificationSenderSelector;
import teamdevhub.devhub.application.selector.verification.VerificationIssuerSelector;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.application.service.verification.vo.IssuedVerification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;
import teamdevhub.devhub.port.in.verification.usecase.VerificationUseCase;
import teamdevhub.devhub.port.out.provider.TimeProvider;
import teamdevhub.devhub.port.out.verification.VerificationRepository;

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
