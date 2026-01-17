package teamdevhub.devhub.fake.pure.sender;

import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.out.sender.NotificationSender;

public class FakeNotificationSender implements NotificationSender {

    private boolean called = false;
    private VerificationTarget target;
    private VerificationMessage message;

    @Override
    public void sendVerification(
            VerificationTarget verificationTarget,
            VerificationMessage verificationMessage
    ) {
        this.called = true;
        this.target = verificationTarget;
        this.message = verificationMessage;
    }

    public boolean isCalled() {
        return called;
    }

    public VerificationTarget getTarget() {
        return target;
    }

    public VerificationMessage getMessage() {
        return message;
    }
}
