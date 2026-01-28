package teamdevhub.devhub.core.auth.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.core.auth.port.in.command.verification.ConfirmVerificationCommand;
import teamdevhub.devhub.core.auth.port.in.command.verification.IssueVerificationCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.verification.VerificationUseCase;
import teamdevhub.devhub.core.notification.port.in.NotificationUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationFacade {

    private final VerificationUseCase verificationUseCase;
    private final NotificationUseCase notificationUseCase;

    public void issueVerification(IssueVerificationCommand issueVerificationCommand) {
        IssuedVerification issuedVerification = verificationUseCase.issueVerification(issueVerificationCommand);
        notificationUseCase.sendVerification(issuedVerification);
    }

    public void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        verificationUseCase.confirmVerification(confirmVerificationCommand);
    }
}
