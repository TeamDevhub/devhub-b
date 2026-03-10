package teamdevhub.devhub.core.notification.port.in;

import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.core.notification.port.in.command.CreateNotificationCommand;

public interface NotificationUseCase {

    void sendVerification(IssuedVerification issuedVerification);

    void checkedNotification(String notificationGuid);

    void createNotification(CreateNotificationCommand notificationCommand);
}
