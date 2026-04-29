package teamdevhub.devhub.api.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.report.domain.Report;

import java.time.LocalDateTime;

@Schema(description = "신고 정보 응답")
@Getter
@Builder
public class AdminReportResponseDto {

    private String reportGuid;
    private String boardGuid;
    private String commentGuid;
    private String reportedUser;
    private String reporterUser;
    private String categoryCd;
    private String reason;
    private boolean processed;
    private LocalDateTime registeredDate;

    public static AdminReportResponseDto fromDomain(Report report) {
        return AdminReportResponseDto.builder()
                .reportGuid(report.getReportGuid())
                .boardGuid(report.getBoardGuid())
                .commentGuid(report.getCommentGuid())
                .reportedUser(report.getReportedUser())
                .reporterUser(report.getReporterUser())
                .categoryCd(report.getCategoryCd())
                .reason(report.getReason())
                .processed(report.isProcessed())
                .registeredDate(report.getAuditInfo().registeredDate())
                .build();
    }
}
