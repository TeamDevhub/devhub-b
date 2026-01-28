package teamdevhub.devhub.core.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.auth.application.service.vo.IssuedVerification;
import teamdevhub.devhub.core.notification.application.selector.NotificationSenderSelector;
import teamdevhub.devhub.core.notification.port.in.NotificationUseCase;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {

    private final NotificationSenderSelector notificationSenderSelector;

    @Override
    public void sendVerification(IssuedVerification issuedVerification) {
        notificationSenderSelector.sendVerification(issuedVerification.verification().getVerificationTarget(), issuedVerification.verificationMessage());
    }
}
