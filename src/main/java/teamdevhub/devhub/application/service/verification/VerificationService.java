package teamdevhub.devhub.application.service.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.application.verification.VerificationIssuerSelector;
import teamdevhub.devhub.port.out.provider.DateTimeProvider;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.IssuedVerification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.verification.SignupVerificationUseCase;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;
import teamdevhub.devhub.port.out.sender.NotificationSender;
import teamdevhub.devhub.port.out.verification.VerificationRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationService implements SignupVerificationUseCase {

    private final VerificationIssuerSelector verificationIssuerSelector;
    private final NotificationSender notificationSender;
    private final DateTimeProvider dateTimeProvider;
    private final VerificationRepository verificationRepository;

    @Override
    public void issueSignupVerification(IssueVerificationCommand issueVerificationCommand) {
        IssuedVerification issuedVerification = verificationIssuerSelector.issueVerification(issueVerificationCommand.verificationTarget());
        verificationRepository.save(issuedVerification.verification());
        issuedVerification.getVerificationMessage()
                .ifPresent(verificationMessage -> notificationSender.sendVerification(issueVerificationCommand.verificationTarget(), verificationMessage));
    }

    @Override
    public void confirmSignupVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        Verification verification = verificationRepository.findByVerificationTarget(confirmVerificationCommand.verificationTarget());
        verification.confirm(confirmVerificationCommand.code(), dateTimeProvider.now());
        verificationRepository.save(verification);
    }

    @Override
    public void assertSignupAllowed(VerificationTarget verificationTarget) {
        Verification verification = verificationRepository.findByVerificationTarget(verificationTarget);
        verification.assertValid(dateTimeProvider.now());
    }

    @Override
    public void consume(VerificationTarget verificationTarget) {
        verificationRepository.deleteByVerificationTarget(verificationTarget);
    }
}
