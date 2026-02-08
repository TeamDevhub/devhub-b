package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.domain.Requirement;

import teamdevhub.devhub.outbound.project.adapter.entity.ProjectEntity;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectSkillEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectMapper {

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

    public static ProjectDetail toProjectDetail(
            ProjectEntity projectEntity,
            List<ProjectSkillEntity> skillEntities,
            List<ProjectRequirementEntity> requirementEntities,
            String likeCount
    ) {
        return ProjectDetail.builder()
                .project(toProject(projectEntity))
                .projectSkill(
                        skillEntities.stream()
                                .map(ProjectSkillEntity::getSkillCd)
                                .filter(Objects::nonNull)
                                .distinct()
                                .toList()
                )
                .projectRequirement(
                        requirementEntities.stream()
                                .map(ProjectMapper::toRequirement)
                                .toList()
                )
                .likeCount(likeCount)
                .build();
    }

}
