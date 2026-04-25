package teamdevhub.devhub.fake.pure.application.port.in.usecase.notification;

import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.core.notification.port.in.NotificationUseCase;
import teamdevhub.devhub.core.notification.port.in.command.CreateNotificationCommand;

import java.util.ArrayList;
import java.util.List;

public class FakeNotificationUseCase implements NotificationUseCase {

    private final List<IssuedVerification> sentVerifications = new ArrayList<>();

    @Override
    public void sendVerification(IssuedVerification issuedVerification) {
        sentVerifications.add(issuedVerification);
    }

    @Override
    public void checkedNotification(String notificationGuid) {
    }

    @Override
    public void createNotification(CreateNotificationCommand notificationCommand) {
    }

    public List<IssuedVerification> getSentVerifications() {
        return sentVerifications;
    }
}
