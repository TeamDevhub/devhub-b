package teamdevhub.devhub.core.report.port.in.command;

import lombok.Builder;

@Builder
public record CreateReportCommand(
        String boardGuid,
        String commentGuid,
        String reportedUser,
        String reporterUser,
        String categoryCd,
        String reason
) {
}
