package teamdevhub.devhub.core.report.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;

@Getter
@Builder
public class Report {

    private final String reportGuid;
    private final String boardGuid;
    private final String commentGuid;
    private final String reportedUser;
    private final String reporterUser;
    private final String categoryCd;
    private final String reason;
    private final boolean processed;
    private final AuditInfo auditInfo;

    public static Report of(
            String reportGuid,
            String boardGuid,
            String commentGuid,
            String reportedUser,
            String reporterUser,
            String categoryCd,
            String reason,
            boolean processed,
            AuditInfo auditInfo
    ) {
        return Report.builder()
                .reportGuid(reportGuid)
                .boardGuid(boardGuid)
                .commentGuid(commentGuid)
                .reportedUser(reportedUser)
                .reporterUser(reporterUser)
                .categoryCd(categoryCd)
                .reason(reason)
                .processed(processed)
                .auditInfo(auditInfo)
                .build();
    }
}
