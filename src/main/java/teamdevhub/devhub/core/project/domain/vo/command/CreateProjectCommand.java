package teamdevhub.devhub.core.project.domain.vo.command;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record CreateProjectCommand(String userGuid, String username, String attachmentFileGuid, String imageFileGuid, String title, String category, String content, String recuritmentTypeCd, String progressTypeCd,
		String progressRegionCd, LocalDateTime recuritmentStartDate, LocalDateTime recuritmentEndDate,
		LocalDateTime progressStartDate, LocalDateTime progressEndDate, List<String> skillList, List<CreateProjectRequirementRequestCommand> positionList) {}
