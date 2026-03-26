package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.project.domain.ProjectLike;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectLikeEntity;

public class ProjectLikeMapper {
	public static ProjectLikeEntity toEntity(ProjectLike projectLike) {
		return ProjectLikeEntity.builder()
				.projectLikeGuid(projectLike.getProjectLikeGuid())
				.projectGuid(projectLike.getProjectGuid())
				.userGuid(projectLike.getUserGuid())
				.build();
	}
	
	public static ProjectLike toProjectLike(ProjectLikeEntity projectLikeEntity) {
		if(projectLikeEntity == null) return null;
		return ProjectLike.builder()
				.projectLikeGuid(projectLikeEntity.getProjectLikeGuid())
				.projectGuid(projectLikeEntity.getProjectGuid())
				.userGuid(projectLikeEntity.getUserGuid())
				.build();
	}
}
