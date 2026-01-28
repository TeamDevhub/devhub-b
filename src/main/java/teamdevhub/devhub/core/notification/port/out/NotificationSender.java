package teamdevhub.devhub.core.notification.port.out;

import teamdevhub.devhub.core.auth.domain.vo.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;

public interface NotificationSender {

    boolean supports(VerificationTarget verificationTarget);
    void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage);
}
