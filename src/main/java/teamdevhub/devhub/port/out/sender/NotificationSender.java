package teamdevhub.devhub.port.out.sender;

import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

public interface NotificationSender {
    void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage);
}
