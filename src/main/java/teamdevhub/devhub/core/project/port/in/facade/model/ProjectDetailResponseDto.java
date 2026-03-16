package teamdevhub.devhub.core.project.port.in.facade.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.ProjectRequirement;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProjectDetailResponseDto extends ProjectBasicResponseDto {

	private String nickName;
	private String mannerDegree;
	private List<String> skillList;
	private List<PositionDto> positionList;
	private String likeCount;
	private boolean capacityClosed;
	private String recruitStatus;

	public static ProjectDetailResponseDto fromDomain(Project project, String imageFileUrl) {
		ProjectDetailResponseDtoBuilder<?, ?> builder = ProjectDetailResponseDto.builder();
		fillBase(builder, project);
		List<PositionDto> positionDtoList = null;
		if (project.getProjectRequirement() != null) {
			positionDtoList = project.getProjectRequirement().stream()
				.map(PositionDto::fromDomain)
				.toList();
		}

		return builder
			.skillList(project.getProjectSkill())
			.positionList(positionDtoList)
			.likeCount(project.getLikeCount())
			.capacityClosed(project.isCapacityClosed())
			.recruitStatus(project.getRecruitStatus())
			.build();
	}

	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class PositionDto {
		private String position;
		private int capacity;
		private String level;
		private boolean full;

		public static PositionDto fromDomain(ProjectRequirement requirement) {
			return PositionDto.builder()
				.position(requirement.getPositionCd())
				.capacity(requirement.getCapacity())
				.level(requirement.getLevelCd())
				.full(false)
				.build();
		}
	}
}
