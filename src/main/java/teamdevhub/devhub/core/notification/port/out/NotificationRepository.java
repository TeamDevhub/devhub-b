package teamdevhub.devhub.core.notification.port.out;

import teamdevhub.devhub.core.notification.domain.Notification;

public interface NotificationRepository {

    Notification getNotification(String notificationGuid);

    void save(Notification notification);
}
