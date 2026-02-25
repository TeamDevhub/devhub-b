package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.Requirement;

import teamdevhub.devhub.outbound.project.adapter.entity.ProjectEntity;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectSkillEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
				.recruitmentStartDate(project.getRecruitmentStartDate().atStartOfDay())
				.recruitmentEndDate(project.getRecruitmentEndDate().atStartOfDay())
				.progressStartDate(project.getProgressStartDate().atStartOfDay())
				.progressEndDate(project.getProgressEndDate().atStartOfDay())
				.deleted(project.isDeleted())
				.capacityClosed(project.isCapacityClosed())
				.build();
	}

    public static Project toProject(ProjectEntity projectEntity, List<String> projectSkills, List<ProjectRequirementEntity> projectRequirementEntityList, String likeCount) {
        return Project.builder()
                .projectGuid(projectEntity.getProjectGuid())
                .userGuid(projectEntity.getUserGuid())
                .username(projectEntity.getUsername())
                .category(projectEntity.getCategory())
                .title(projectEntity.getTitle())
                .content(projectEntity.getContent())
                .recruitmentTypeCd(projectEntity.getRecruitmentTypeCd())
                .recruitmentStartDate(projectEntity.getRecruitmentStartDate().toLocalDate())
                .recruitmentEndDate(projectEntity.getRecruitmentEndDate().toLocalDate())
                .progressTypeCd(projectEntity.getProgressTypeCd())
//                .progressRegionCd(projectEntity.getpro)
//                .progressPeriod(projectEntity.getpro)
                .progressStartDate(projectEntity.getProgressStartDate().toLocalDate())
                .progressEndDate(projectEntity.getProgressEndDate().toLocalDate())
                .likeCount(likeCount)
                .projectSkill(projectSkills)
                .projectRequirement(projectRequirementEntityList.stream().map(ProjectMapper::toRequirement).toList())
                .auditInfo(toAuditInfo(projectEntity))
                .build();
    }

    private static AuditInfo toAuditInfo(ProjectEntity entity) {
        return AuditInfo.of(
                entity.getRegistrantGuid(),
                entity.getRegisteredDate(),
                entity.getModifierGuid(),
                entity.getModifiedDate()
        );
    }
	
    public static Project toProject(ProjectEntity entity) {
        if (entity == null) return null;
        return Project.builder()
                .projectGuid(entity.getProjectGuid())
                .userGuid(entity.getUserGuid())
//                .fileGuid(entity.get)
                .category(entity.getCategory())
                .title(entity.getTitle())
                .content(entity.getContent())
                .recruitmentTypeCd(entity.getRecruitmentTypeCd())
                .recruitmentStartDate(entity.getProgressStartDate().toLocalDate())
                .recruitmentEndDate(entity.getRecruitmentEndDate().toLocalDate())
                .progressTypeCd(entity.getProgressTypeCd())
//                .progressRegionCd(entity.getpro)
//                .progressPeriod(entity.getpro)
                .progressStartDate(entity.getProgressStartDate().toLocalDate())
                .progressEndDate(entity.getProgressEndDate().toLocalDate())
                .auditInfo(toAuditInfo(entity))
                .build();
    }

    public static Requirement toRequirement(ProjectRequirementEntity entity) {
        if (entity == null) return null;
        return Requirement.builder()
                .projectRequirementGuid(entity.getProjectRequirementGuid())
                .projectGuid(entity.getProjectGuid())
                .positionCd(entity.getPositionCd())
                .levelCd(entity.getLevelCd())
                .capacity(entity.getCapacity())
                .build();
    }

    public static Map<String, List<String>> toMapSkill(List<ProjectSkillEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .collect(Collectors.groupingBy(
                        ProjectSkillEntity::getProjectGuid,
                        Collectors.mapping(ProjectSkillEntity::getSkillCd, Collectors.toList())
                ));
    }

    public static Map<String, List<ProjectRequirementEntity>> toMapRequirement(List<ProjectRequirementEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .collect(Collectors.groupingBy(ProjectRequirementEntity::getProjectGuid));
    }

    public static Project toProjectDetail(
            ProjectEntity projectEntity,
            List<ProjectSkillEntity> skillEntities,
            List<ProjectRequirementEntity> requirementEntities,
            String likeCount
    ) {
            if (projectEntity == null) return null;
        return Project.builder()
                .projectGuid(projectEntity.getProjectGuid())
                .userGuid(projectEntity.getUserGuid())
                .username(projectEntity.getUsername())
                .category(projectEntity.getCategory())
                .title(projectEntity.getTitle())
                .content(projectEntity.getContent())
                .recruitmentTypeCd(projectEntity.getRecruitmentTypeCd())
                .recruitmentStartDate(projectEntity.getRecruitmentStartDate().toLocalDate())
                .recruitmentEndDate(projectEntity.getRecruitmentEndDate().toLocalDate())
                .progressTypeCd(projectEntity.getProgressTypeCd())
                .progressRegionCd(projectEntity.getProgressRegionCd())
                .progressStartDate(projectEntity.getProgressStartDate().toLocalDate())
                .progressEndDate(projectEntity.getProgressEndDate().toLocalDate())
                .likeCount(likeCount)
                .projectSkill(
                        skillEntities == null ? List.of() :
                                skillEntities.stream()
                                        .map(ProjectSkillEntity::getSkillCd)
                                        .filter(Objects::nonNull)
                                        .distinct()
                                        .toList()
                )
                .projectRequirement(
                        requirementEntities == null ? List.of() :
                                requirementEntities.stream()
                                        .map(ProjectMapper::toRequirement)
                                        .toList()
                )
                .auditInfo(toAuditInfo(projectEntity))
                .build();
    }

}
