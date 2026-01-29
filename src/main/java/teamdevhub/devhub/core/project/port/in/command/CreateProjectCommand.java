package teamdevhub.devhub.core.project.port.in.command;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import teamdevhub.devhub.api.project.model.request.CreateProjectRequirementRequestDto;

@Builder
public record CreateProjectCommand(String title, String category, String content, String recuritmentTypeCd, String progressTypeCd,
		String progressRegionCd, LocalDateTime recuritmentStartDate, LocalDateTime recuritmentEndDate,
		LocalDateTime progressStartDate, LocalDateTime progressEndDate, List<String> skillList, List<CreateProjectRequirementRequestDto> positionList) {}
