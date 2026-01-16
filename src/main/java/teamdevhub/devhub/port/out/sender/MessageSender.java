package teamdevhub.devhub.port.out.sender;

import teamdevhub.devhub.domain.verification.VerificationMessage;
import teamdevhub.devhub.domain.verification.VerificationTarget;

import java.util.Map;

public interface MessageSender {

    boolean supports(VerificationTarget target);
    void sendVerification(VerificationTarget target, VerificationMessage verificationMessage);
}
