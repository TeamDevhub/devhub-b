package teamdevhub.devhub.outbound.report.adapter.mapper;

import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.report.domain.Report;
import teamdevhub.devhub.outbound.board.adapter.entity.ReportEntity;

public class ReportMapper {

    private ReportMapper() {}

    public static Report toDomain(ReportEntity entity) {
        return Report.of(
                entity.getReportGuid(),
                entity.getBoardGuid(),
                entity.getCommentGuid(),
                entity.getReportedUser(),
                entity.getReporterUser(),
                entity.getCategoryCd(),
                entity.getReason(),
                entity.isProcessed(),
                AuditInfo.of(
                        entity.getRegistrantGuid(),
                        entity.getRegisteredDate(),
                        entity.getModifierGuid(),
                        entity.getModifiedDate()
                )
        );
    }
}
