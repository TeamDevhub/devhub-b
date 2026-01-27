package teamdevhub.devhub.fake.pure.selector;

import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.notification.application.selector.NotificationSenderSelector;

public class FakeNotificationSenderSelector implements NotificationSenderSelector {

    private boolean sent = false;
    private VerificationTarget target;
    private VerificationMessage message;

    @Override
    public void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage) {
        this.sent = true;
        this.target = verificationTarget;
        this.message = verificationMessage;
    }

    public boolean isSent() {
        return sent;
    }

    public VerificationMessage getMessage() {
        return message;
    }
}
