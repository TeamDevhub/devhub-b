package teamdevhub.devhub.core.admin.banner.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;

@Getter
@Builder
public class Banner {
    private String bannerGuid;
    private String imageGuid;
    private String publicationStartDate;
    private String publicationEndDate;
    private boolean isUsed;
    private boolean isMainBanner;
    private String description;
    private String title;
    private String link;
    private AuditInfo auditInfo;
}
