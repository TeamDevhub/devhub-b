package teamdevhub.devhub.port.out.selector;

import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

public interface NotificationSenderSelector {

    void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage);
}
