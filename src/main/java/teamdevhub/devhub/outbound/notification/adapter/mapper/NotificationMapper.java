package teamdevhub.devhub.outbound.notification.adapter.mapper;

import teamdevhub.devhub.core.notification.domain.Notification;
import teamdevhub.devhub.outbound.notification.adapter.entity.NotificationEntity;

import java.time.format.DateTimeFormatter;

public class NotificationMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Notification toNotification(NotificationEntity notificationEntity) {
        return Notification.builder()
                .notificationGuid(notificationEntity.getNotificationGuid())
                .content(notificationEntity.getContent())
                .isChecked(notificationEntity.isChecked())
                .typeCd(notificationEntity.getTypeCd())
                .receiver(notificationEntity.getReceiver())
                .registrantGuid(notificationEntity.getRegistrantGuid())
                .registrationDate(notificationEntity.getRegisteredDate().format(formatter))
                .build();
    }

}
