package teamdevhub.devhub.core.notification.application.selector;

import teamdevhub.devhub.core.auth.domain.vo.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;

public interface NotificationSenderSelector {

    void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage);
}
