package teamdevhub.devhub.core.notification.port.in;

import teamdevhub.devhub.core.notification.domain.Notification;

import java.util.List;

public interface NotificationQueryUseCase {

    List<Notification> getNotificationList(String userGuid);
}
