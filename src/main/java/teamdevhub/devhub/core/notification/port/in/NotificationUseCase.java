package teamdevhub.devhub.core.notification.port.in;

import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;

public interface NotificationUseCase {

    void sendVerification(IssuedVerification issuedVerification);

    void checkedNotification(String userGuid);
}
