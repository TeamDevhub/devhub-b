package teamdevhub.devhub.core.project.port.in.command;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;

@Builder
public record SearchProjectListCommand(
			String order,
			String keyword,
			List<String> skillCodeList,
			List<String> regionCodeList,
			List<String> positionCodeList,
			List<String> progressPeriodList,
			List<String> positionLevelCodeList,
			List<String> projectRecruitTypeList,
			List<String> projectProgressTypeList,
			List<String> projectRecruitStatusList,
			LocalDate recruitmentStartDate,
			LocalDate recruitmentEndDate,
			LocalDate progressStartDate
		) {
}
