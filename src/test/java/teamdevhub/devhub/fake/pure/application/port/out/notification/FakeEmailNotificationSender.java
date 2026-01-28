package teamdevhub.devhub.fake.pure.application.port.out.notification;

import teamdevhub.devhub.core.notification.port.out.NotificationSender;
import teamdevhub.devhub.core.auth.domain.vo.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.VerificationType;

import java.util.ArrayList;
import java.util.List;

public class FakeEmailNotificationSender implements NotificationSender {

    private final List<String> sentEmails = new ArrayList<>();

    @Override
    public boolean supports(VerificationTarget verificationTarget) {
        return verificationTarget.verificationType() == VerificationType.EMAIL;
    }

    @Override
    public void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage) {
        sentEmails.add(verificationTarget.value());
    }

    public boolean wasSentTo(String email) {
        return sentEmails.contains(email);
    }
}
