package teamdevhub.devhub.core.project.port.in.facade.model;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.shared.enums.ProjectApprovalStatus;

@Getter
@SuperBuilder
@NoArgsConstructor
public class UserProjectResponseDto extends ProjectBasicResponseDto{
	
	private String recruitStatus;
	
	private String currentRecriutNumber;
	private String totalRecriutNumber;
	private String applicantNumber;
	private String approvalNumber;
	private String approvalState;
	private String progressState;
	
	private List<ProjectApplicationScore> applicationList;

	public static UserProjectResponseDto fromDomain(Project project, List<ProjectApplicationScore> projectApplicationList, String approvalState) {
		UserProjectResponseDtoBuilder<?, ?> builder = UserProjectResponseDto.builder();
		fillBase(builder, project);
		if(projectApplicationList == null) {
		    projectApplicationList = Collections.emptyList();
		}
		long getCurrentRecruitNumber = projectApplicationList.stream().filter(item -> ProjectApprovalStatus.APPROVED.getCode().equals(item.getStatusCd())).count();
		long getTotalRecriutNumber =
				project.getProjectRequirement().stream()
			        .mapToInt(item -> item.getCapacity())
			        .sum();
		long getApprovalNumber = projectApplicationList.stream().filter(item -> ProjectApprovalStatus.PENDING.getCode().equals(item.getStatusCd())).count();
		String getProgressState = LocalDate.now().isBefore(project.getProgressEndDate()) ? "ing" : "end";

		return builder
			.recruitStatus(project.getRecruitStatus())
			.currentRecriutNumber(String.valueOf(getCurrentRecruitNumber))
			.totalRecriutNumber(String.valueOf(getTotalRecriutNumber))
			.applicantNumber(String.valueOf(projectApplicationList.size()))
			.approvalNumber(String.valueOf(getApprovalNumber))
			.approvalState(approvalState)
			.progressState(getProgressState)
			.applicationList(projectApplicationList)
			.build();
	}
}
