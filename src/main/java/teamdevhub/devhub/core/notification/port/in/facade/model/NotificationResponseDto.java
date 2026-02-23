package teamdevhub.devhub.core.notification.port.in.facade.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.notification.domain.Notification;

@Getter
@SuperBuilder
@NoArgsConstructor
public class NotificationResponseDto {

    private String notificationGuid;
    private String typeCd;
    private String content;
    private String registrationDate;

    public static NotificationResponseDto fromDomain(Notification notification) {
        return builder()
                .notificationGuid(notification.getNotificationGuid())
                .typeCd(notification.getTypeCd())
                .content(notification.getContent())
                .registrationDate(notification.getRegistrationDate())
                .build();
    }

}
