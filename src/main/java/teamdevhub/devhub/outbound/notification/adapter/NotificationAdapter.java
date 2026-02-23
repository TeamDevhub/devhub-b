package teamdevhub.devhub.outbound.notification.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.notification.domain.Notification;
import teamdevhub.devhub.core.notification.port.out.NotificationRepository;
import teamdevhub.devhub.outbound.notification.adapter.mapper.NotificationMapper;
import teamdevhub.devhub.outbound.notification.persistence.JpaNotificationRepository;

@Component
@RequiredArgsConstructor
public class NotificationAdapter implements NotificationRepository {

    private final JpaNotificationRepository jpaNotificationRepository;

    @Override
    public Notification getNotification(String notificationGuid) {
        return NotificationMapper.toDomain(jpaNotificationRepository.findAllByNotificationGuid(notificationGuid));
    }

    @Override
    public void save(Notification notification) {
        jpaNotificationRepository.save(NotificationMapper.toEntity(notification));
    }
}
