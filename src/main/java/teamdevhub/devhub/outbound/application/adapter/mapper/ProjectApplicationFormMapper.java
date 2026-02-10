package teamdevhub.devhub.outbound.application.adapter.mapper;

import teamdevhub.devhub.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationFormEntity;

public class ProjectApplicationFormMapper {
	
	public static ProjectApplicationFormEntity toEntity(ProjectApplicationForm projectApplicationForm) {
		return ProjectApplicationFormEntity.builder()
				.projectApplicationFormGuid(projectApplicationForm.projectApplicationFormGuid())
				.projectGuid(projectApplicationForm.projectGuid())
				.applicationFormGuid(projectApplicationForm.applicationFormGuid())
				.build();
	}
	
	public static ProjectApplicationForm toProjectApplicationForm(ProjectApplicationFormEntity projectApplicationFormEntity) {
		return ProjectApplicationForm.builder()
				.projectGuid(projectApplicationFormEntity.getProjectGuid())
				.applicationFormGuid(projectApplicationFormEntity.getApplicationFormGuid())
				.build();
	}

}
