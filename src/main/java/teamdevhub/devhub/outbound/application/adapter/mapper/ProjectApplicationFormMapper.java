package teamdevhub.devhub.outbound.application.adapter.mapper;

import teamdevhub.devhub.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationFormEntity;

public class ProjectApplicationFormMapper {
	
	public static ProjectApplicationFormEntity toEntity(ProjectApplicationForm projectApplicationForm) {
		return ProjectApplicationFormEntity.builder()
				.projectApplicationFormGuid(projectApplicationForm.getProjectApplicationFormGuid())
				.projectGuid(projectApplicationForm.getProjectGuid())
				.applicationFormGuid(projectApplicationForm.getApplicationFormGuid())
				.build();
	}
	
	public static ProjectApplicationForm toProjectApplicationForm(ProjectApplicationFormEntity projectApplicationFormEntity) {
		return ProjectApplicationForm.builder()
				.projectApplicationFormGuid(projectApplicationFormEntity.getProjectApplicationFormGuid())
				.projectGuid(projectApplicationFormEntity.getProjectGuid())
				.applicationFormGuid(projectApplicationFormEntity.getApplicationFormGuid())
				.build();
	}

}
