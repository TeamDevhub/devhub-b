package teamdevhub.devhub.core.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.core.notification.application.selector.NotificationSenderSelector;
import teamdevhub.devhub.core.notification.domain.Notification;
import teamdevhub.devhub.core.notification.port.in.NotificationUseCase;
import teamdevhub.devhub.core.notification.port.out.NotificationRepository;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {

    private final NotificationSenderSelector notificationSenderSelector;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendVerification(IssuedVerification issuedVerification) {
        notificationSenderSelector.sendVerification(issuedVerification.verification().getVerificationTarget(), issuedVerification.verificationMessage());
    }

    @Override
    public void checkedNotification(String userGuid) {
        Notification notification = notificationRepository.getNotification(userGuid);
        notification.checked();
        notificationRepository.save(notification);
    }
}
