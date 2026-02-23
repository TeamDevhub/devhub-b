package teamdevhub.devhub.core.notification.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Notification {

    private String notificationGuid;
    private String typeCd;
    private String receiver;
    private String content;
    private boolean isChecked;
    private String registrantGuid;
    private String registrationDate;

}
