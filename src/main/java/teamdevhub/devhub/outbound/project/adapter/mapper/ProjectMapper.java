package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectEntity;

public class ProjectMapper {
	
	public static ProjectEntity toEntity(Project project) {
		return ProjectEntity.builder()
				.projectGuid(project.getProjectGuid())
				.attachmentFileGuid(project.getAttachmentFileGuid())
				.imageFileGuid(project.getImageFileGuid())
				.userGuid(project.getUserGuid())
				.recruitmentTypeCd(project.getRecruitmentTypeCd())
				.progressTypeCd(project.getProgressTypeCd())
				.progressRegionCd(project.getProgressRegionCd())
				.username(project.getUsername())
				.category(project.getCategory())
				.title(project.getTitle())
				.content(project.getContent())
				.recuritmentStartDate(project.getRecuritmentStartDate())
				.recuritmentEndDate(project.getRecuritmentEndDate())
				.progressStartDate(project.getProgressStartDate())
				.progressEndDate(project.getProgressEndDate())
				.deleted(project.isDeleted())
				.capacityClosed(project.isCapacityClosed())
				.build();
	}
}
