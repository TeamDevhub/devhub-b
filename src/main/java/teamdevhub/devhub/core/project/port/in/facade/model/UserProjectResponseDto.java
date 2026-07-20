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

	// 내가 신청한 프로젝트(apply) 목록에서만 채워진다 - 지원 취소 시 어떤 지원 건인지 식별하기 위함
	private String applicationGuid;

	private List<ProjectApplicationScore> applicationList;

	public static UserProjectResponseDto fromDomain(Project project, List<ProjectApplicationScore> projectApplicationList, String approvalState) {
		return fromDomain(project, projectApplicationList, approvalState, null);
	}

	public static UserProjectResponseDto fromDomain(Project project, List<ProjectApplicationScore> projectApplicationList, String approvalState, String applicationGuid) {
		UserProjectResponseDtoBuilder<?, ?> builder = UserProjectResponseDto.builder();
		fillBase(builder, project);
		if(projectApplicationList == null) {
		    projectApplicationList = Collections.emptyList();
		}
		long getCurrentRecruitNumber = 0L;
		long getTotalRecriutNumber = 0L;
		long getApprovalNumber = 0L;
		if(projectApplicationList.size() > 0) {
			getCurrentRecruitNumber = projectApplicationList.stream().filter(item -> ProjectApprovalStatus.APPROVED.getCode().equals(item.getStatusCd())).count();
		}
		if(project.getProjectRequirement() != null && project.getProjectRequirement().size() > 0) {
			getTotalRecriutNumber =
					project.getProjectRequirement().stream()
					.mapToInt(item -> item.getCapacity())
					.sum();
		}
		if(projectApplicationList.size() > 0) {
			getApprovalNumber = projectApplicationList.stream().filter(item -> ProjectApprovalStatus.PENDING.getCode().equals(item.getStatusCd())).count();
		}
		String getProgressState = LocalDate.now().isBefore(project.getProgressEndDate()) ? "ing" : "end";

		return builder
			.recruitStatus(project.getRecruitStatus())
			.currentRecriutNumber(String.valueOf(getCurrentRecruitNumber))
			.totalRecriutNumber(String.valueOf(getTotalRecriutNumber))
			.applicantNumber(String.valueOf(projectApplicationList.size()))
			.approvalNumber(String.valueOf(getApprovalNumber))
			.approvalState(approvalState)
			.progressState(getProgressState)
			.applicationGuid(applicationGuid)
			.applicationList(projectApplicationList)
			.build();
	}
}
