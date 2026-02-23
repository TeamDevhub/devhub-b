package teamdevhub.devhub.outbound.notification.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.notification.domain.Notification;
import teamdevhub.devhub.core.notification.port.out.NotificationQueryRepository;
import teamdevhub.devhub.outbound.notification.adapter.mapper.NotificationMapper;
import teamdevhub.devhub.outbound.notification.persistence.JpaNotificationRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationQueryAdapter implements NotificationQueryRepository {

    private final JpaNotificationRepository jpaNotificationRepository;

    @Override
    public List<Notification> getNotificationList(String userGuid) {
        return jpaNotificationRepository.findAllByReceiverAndCheckedFalse(userGuid).stream()
                .map(NotificationMapper::toNotification)
                .toList();
    }
}
