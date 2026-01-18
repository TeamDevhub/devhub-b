package teamdevhub.devhub.port.in.verification;

import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;

public interface VerificationUseCase {

    void issueVerification(IssueVerificationCommand issueVerificationCommand);
    void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand);
    void assertAllowed(VerificationTarget verificationTarget);
    void consume(VerificationTarget verificationTarget);
}
