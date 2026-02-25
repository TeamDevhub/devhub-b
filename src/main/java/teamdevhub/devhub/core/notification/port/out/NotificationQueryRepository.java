package teamdevhub.devhub.core.notification.port.out;

import teamdevhub.devhub.core.notification.domain.Notification;

import java.util.List;

public interface NotificationQueryRepository {

    List<Notification> getNotificationList(String userGuid);
}
