package teamdevhub.devhub.core.notification.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;

@Getter
@Builder
public class Notification {

    private String notificationGuid;
    private String typeCd;
    private String receiver;
    private String content;
    private boolean isChecked;
    private final AuditInfo auditInfo;

    public void checked() {
        this.isChecked = true;
    }
}
