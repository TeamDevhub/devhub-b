package teamdevhub.devhub.core.notification.port.out;

import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;

public interface MessageSender {

    boolean supports(VerificationTarget verificationTarget);
    void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage);
}
