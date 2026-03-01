package teamdevhub.devhub.outbound.notification.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.notification.adapter.entity.NotificationEntity;

import java.util.List;

public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, String> {

    List<NotificationEntity> findAllByReceiverAndCheckedFalse(String userGuid);

    NotificationEntity findAllByNotificationGuid(String notificationGuid);
}
