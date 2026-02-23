package teamdevhub.devhub.api.project.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchProjectRequestDto {
	
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
0    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentEndDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate progressStartDate;

	public SearchProjectListCommand toSearchProjectListCommand() {
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
