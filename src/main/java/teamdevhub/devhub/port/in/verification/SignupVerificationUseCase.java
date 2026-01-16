package teamdevhub.devhub.port.in.verification;

import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;

public interface SignupVerificationUseCase {
    void issueSignupVerification(IssueVerificationCommand issueVerificationCommand);
    void confirmSignupVerification(ConfirmVerificationCommand confirmVerificationCommand);
    void validateSignupVerification(VerificationTarget verificationTarget);
}
