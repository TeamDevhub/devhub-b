package teamdevhub.devhub.core.project.port.in.facade.model;

import java.util.List;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.admin.form.port.in.facade.model.ApplicationFormResponseDto;
import teamdevhub.devhub.core.project.domain.Project;

@Getter
@SuperBuilder
public class ProjectDetailWithFormResponseDto extends ProjectDetailResponseDto {
	
	private List<String> applicationFormList;
	private List<ApplicationFormResponseDto> additionalFormList;
	private String imageFileName;
	private String attachmentFileName;
	
	public static ProjectDetailWithFormResponseDto fromDomain(Project project, List<String> applicationFormList, List<ApplicationFormResponseDto> additionalFormList
			, String imageFileName, String attachmentFilename) {
		ProjectDetailWithFormResponseDtoBuilder<?, ?> builder = ProjectDetailWithFormResponseDto.builder();
		fillBase(builder, project);
		return builder
			.skillList(project.getProjectSkill())
			.positionList(project.getProjectRequirement().stream().map(RequirementResponseDto::fromDomain).toList())
			.likeCount(project.getLikeCount())
			.capacityClosed(project.isCapacityClosed())
			.recruitStatus(project.getRecruitStatus())
			.applicationFormList(applicationFormList)
			.additionalFormList(additionalFormList)
			.imageFileName(imageFileName)
			.attachmentFileName(attachmentFilename)
			.build();
	}
}