package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.Requirement;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectEntity;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectSkillEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static Project toProject(ProjectEntity entity) {
        if (entity == null) return null;
        return Project.builder()
                .projectGuid(entity.getProjectGuid())
                .userGuid(entity.getUserGuid())
//                .username(entity.get)
                .category(entity.getCategory())
                .title(entity.getTitle())
                .content(entity.getContent())
                .recruitmentTypeCd(entity.getRecuritmentTypeCd())
                .recruitmentStartDate(entity.getProgressStartDate())
                .recruitmentEndDate(entity.getRecuritmentEndDate())
                .progressTypeCd(entity.getProgressTypeCd())
//                .progressRegionCd(entity.getpro)
//                .progressPeriod(entity.getpro)
                .progressStartDate(entity.getProgressStartDate())
                .progressEndDate(entity.getProgressEndDate())
                .auditInfo(toAuditInfo(entity))
                .build();
    }

    public static Project toProject(ProjectEntity projectEntity, List<String> projectSkills, List<ProjectRequirementEntity> projectRequirementEntityList, String likeCount) {
        return Project.builder()
                .projectGuid(projectEntity.getProjectGuid())
                .userGuid(projectEntity.getUserGuid())
                .username(projectEntity.getNickname())
                .category(projectEntity.getCategory())
                .title(projectEntity.getTitle())
                .content(projectEntity.getContent())
                .recruitmentTypeCd(projectEntity.getRecuritmentTypeCd())
                .recruitmentStartDate(projectEntity.getProgressStartDate())
                .recruitmentEndDate(projectEntity.getRecuritmentEndDate())
                .progressTypeCd(projectEntity.getProgressTypeCd())
//                .progressRegionCd(projectEntity.getpro)
//                .progressPeriod(projectEntity.getpro)
                .progressStartDate(projectEntity.getProgressStartDate())
                .progressEndDate(projectEntity.getProgressEndDate())
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
}
