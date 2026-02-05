package teamdevhub.devhub.core.project.domain.vo.command;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record CreateProjectCommand(String userGuid, String username, String attachmentFileGuid, String imageFileGuid, String title, String category, String content, String recruitmentTypeCd, String progressTypeCd,
		String progressRegionCd, LocalDateTime recruitmentStartDate, LocalDateTime recruitmentEndDate,
		LocalDateTime progressStartDate, LocalDateTime progressEndDate, List<String> skillList, List<CreateProjectRequirementRequestCommand> positionList) {}
