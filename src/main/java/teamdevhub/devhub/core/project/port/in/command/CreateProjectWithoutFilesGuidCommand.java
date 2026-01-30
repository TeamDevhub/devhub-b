package teamdevhub.devhub.core.project.port.in.command;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementRequestCommand;

@Builder
public record CreateProjectWithoutFilesGuidCommand(String userGuid, String username, String title, String category, String content, String recuritmentTypeCd, String progressTypeCd,
		String progressRegionCd, LocalDateTime recuritmentStartDate, LocalDateTime recuritmentEndDate,
		LocalDateTime progressStartDate, LocalDateTime progressEndDate, List<String> skillList, List<CreateProjectRequirementRequestCommand> positionList) {
	
	public CreateProjectCommand toCreateProjectCommand(String attachmentFileGuid, String imageFileGuid) {
		return CreateProjectCommand.builder()
				.userGuid(this.userGuid)
				.username(this.username)
				.attachmentFileGuid(attachmentFileGuid)
				.imageFileGuid(imageFileGuid)
				.title(this.title)
				.category(this.category)
				.content(this.content)
				.recuritmentTypeCd(this.recuritmentTypeCd)
				.progressTypeCd(this.progressTypeCd)
				.progressRegionCd(this.progressRegionCd)
				.recuritmentStartDate(this.recuritmentStartDate)
				.recuritmentEndDate(this.recuritmentEndDate)
				.progressStartDate(this.progressStartDate)
				.progressEndDate(this.progressEndDate)
				.skillList(this.skillList)
				.positionList(this.positionList)
				.build();
	}
}
