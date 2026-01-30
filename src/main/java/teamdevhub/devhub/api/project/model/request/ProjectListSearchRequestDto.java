package teamdevhub.devhub.api.project.model.request;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListSearchRequestDto {
	
	private String order;
    private String keyword;
    private List<String> skillCodeList;
    private List<String> regionCodeList;
    private List<String> positionCodeList;
    private List<String> progressPeriodList;
    private List<String> positionLevelCodeList;
    private List<String> projectRecruitTypeList;
    private List<String> projectProgressTypeList;
    private List<String> projectRecruitStatusList;
    private LocalDate recruitmentStartDate;
    private LocalDate recruitmentEndDate;
    private LocalDate progressStartDate;

	public SearchProjectListCommand toSearchProjectListCommaond() {
		return SearchProjectListCommand.builder()
				.order(order)
				.keyword(keyword)
				.skillCodeList(skillCodeList)
				.regionCodeList(regionCodeList)
				.positionCodeList(positionCodeList)
				.progressPeriodList(progressPeriodList)
				.positionLevelCodeList(positionLevelCodeList)
				.projectRecruitTypeList(projectRecruitTypeList)
				.projectProgressTypeList(projectProgressTypeList)
				.projectRecruitStatusList(projectRecruitStatusList)
				.recruitmentStartDate(recruitmentStartDate)
				.recruitmentEndDate(recruitmentEndDate)
				.progressStartDate(progressStartDate)
				.build();
	}
}
