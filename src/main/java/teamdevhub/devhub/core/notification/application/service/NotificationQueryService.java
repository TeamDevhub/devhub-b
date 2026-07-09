package teamdevhub.devhub.core.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.notification.domain.Notification;
import teamdevhub.devhub.core.notification.port.in.NotificationQueryUseCase;
import teamdevhub.devhub.core.notification.port.out.NotificationQueryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationQueryService implements NotificationQueryUseCase {

    private final NotificationQueryRepository notificationQueryRepository;

    @Override
    public List<Notification> getNotificationList(String userGuid) {
        return notificationQueryRepository.getNotificationList(userGuid);
    }
}
