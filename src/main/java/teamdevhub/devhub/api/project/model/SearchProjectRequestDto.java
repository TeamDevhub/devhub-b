package teamdevhub.devhub.api.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentEndDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate progressStartDate;

	public SearchProjectListCommand toSearchProjectListCommand() {
		LocalDateTime formattedRecruitmentStartDate = null;
		LocalDateTime formattedRecruitmentEndDate = null;
		LocalDateTime formattedProgressStartDate = null;
		if(recruitmentStartDate != null) {
			formattedRecruitmentStartDate = recruitmentStartDate.atStartOfDay();
		} 
		if(recruitmentEndDate != null) {
			formattedRecruitmentEndDate = recruitmentEndDate.atStartOfDay();
		} 
		if(progressStartDate != null) {
			formattedProgressStartDate = progressStartDate.atStartOfDay();
		} 
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
				.recruitmentStartDate(formattedRecruitmentStartDate)
				.recruitmentEndDate(formattedRecruitmentEndDate)
				.progressStartDate(formattedProgressStartDate)
				.build();
	}
}
