package teamdevhub.devhub.core.notification.port.in;

import teamdevhub.devhub.core.auth.application.service.vo.IssuedVerification;

public interface NotificationUseCase {

    void sendVerification(IssuedVerification issuedVerification);
}
