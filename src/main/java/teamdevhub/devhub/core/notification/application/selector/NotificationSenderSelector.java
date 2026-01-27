package teamdevhub.devhub.core.notification.application.selector;

import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;

public interface NotificationSenderSelector {

    void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage);
}
