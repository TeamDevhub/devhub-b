package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.port.in.command.ConfirmVerificationCommand;
import teamdevhub.devhub.core.auth.port.in.command.IssueVerificationCommand;

public interface VerificationUseCase {

    void issueVerification(IssueVerificationCommand issueVerificationCommand);
    void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand);
    void assertAllowed(VerificationTarget verificationTarget);
    void consume(VerificationTarget verificationTarget);
}
