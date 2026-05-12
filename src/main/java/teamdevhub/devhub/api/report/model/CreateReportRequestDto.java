package teamdevhub.devhub.api.report.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.core.report.port.in.command.CreateReportCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReportRequestDto {

    private String boardGuid;

    private String commentGuid;

    @NotBlank(message = "신고당한 회원은 필수입니다.")
    private String reportedUser;

    @NotBlank(message = "카테고리 설정은 필수입니다")
    private String categoryCd;

    private String reason;

    public CreateReportCommand toCommand(String reporterUser) {
        return CreateReportCommand.builder()
                .boardGuid(this.boardGuid)
                .commentGuid(this.commentGuid)
                .reportedUser(this.reportedUser)
                .reporterUser(reporterUser)
                .categoryCd(this.categoryCd)
                .reason(this.reason)
                .build();
    }
}
