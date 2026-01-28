package teamdevhub.devhub.core.auth.port.in.usecase.verification;

import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;
import teamdevhub.devhub.core.auth.port.in.command.verification.ConfirmVerificationCommand;
import teamdevhub.devhub.core.auth.port.in.command.verification.IssueVerificationCommand;

public interface VerificationUseCase {

    IssuedVerification issueVerification(IssueVerificationCommand issueVerificationCommand);
    void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand);
    void assertAllowed(VerificationTarget verificationTarget);
    void consume(VerificationTarget verificationTarget);
}
