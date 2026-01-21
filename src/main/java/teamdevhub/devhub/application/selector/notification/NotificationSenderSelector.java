package teamdevhub.devhub.application.selector.notification;

import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

public interface NotificationSenderSelector {

    void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage);
}
