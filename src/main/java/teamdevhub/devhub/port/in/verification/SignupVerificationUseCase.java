package teamdevhub.devhub.port.in.verification;

import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;

public interface SignupVerificationUseCase {
    void issueSignupVerification(IssueVerificationCommand issueVerificationCommand);
    void confirmSignupVerification(ConfirmVerificationCommand confirmVerificationCommand);
    void assertSignupAllowed(VerificationTarget verificationTarget);
    void consume(VerificationTarget verificationTarget);
}
