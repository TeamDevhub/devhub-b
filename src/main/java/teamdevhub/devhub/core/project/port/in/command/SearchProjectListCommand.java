package teamdevhub.devhub.core.project.port.in.command;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record SearchProjectListCommand(
			String order,
			String keyword,
			List<String> skillCodeList,
			List<String> regionCodeList,
			List<String> positionCodeList,
			// 추가예정
			List<String> progressPeriodList,
			List<String> positionLevelCodeList,
			List<String> projectRecruitTypeList,
			List<String> projectProgressTypeList,
			// 추가예정
			List<String> projectRecruitStatusList,
			LocalDateTime recruitmentStartDate,
			LocalDateTime recruitmentEndDate,
			LocalDateTime progressStartDate
		) {
}
