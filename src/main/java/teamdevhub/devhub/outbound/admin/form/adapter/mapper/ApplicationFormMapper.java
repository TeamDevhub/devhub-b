package teamdevhub.devhub.outbound.admin.form.adapter.mapper;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;

public class ApplicationFormMapper {
	
	public static ApplicationFormEntity toEntity(ApplicationForm applicationForm) {
		return ApplicationFormEntity.builder()
				.applicationFormGuid(applicationForm.getApplicationFormGuid())
				.typeCd(applicationForm.getTypeCd())
				.title(applicationForm.getTitle())
				.helpText(applicationForm.getHelpText())
				.isCustomized(applicationForm.isCustomYn())
				.isUsed(applicationForm.isUseYn())
				.build();
	}
	
	public static ApplicationForm toApplicationForm(ApplicationFormEntity applicationFormEntity) {
		return ApplicationForm.builder()
				.applicationFormGuid(applicationFormEntity.getApplicationFormGuid())
				.typeCd(applicationFormEntity.getTypeCd())
				.title(applicationFormEntity.getTitle())
				.helpText(applicationFormEntity.getHelpText())
				.customYn(applicationFormEntity.isCustomized())
				.useYn(applicationFormEntity.isUsed())
				.build();
	}

}
