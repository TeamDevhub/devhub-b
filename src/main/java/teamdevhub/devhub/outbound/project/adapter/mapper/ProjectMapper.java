package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.Requirement;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectEntity;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.outbound.project.persistence.ProjectQueryRepositoryImpl.ProjectDetailFlatDto;

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

    public static Project toProject(ProjectDetailFlatDto dto) {
        return Project.builder()
                .projectGuid(dto.projectEntity().getProjectGuid())
                .userGuid(dto.projectEntity().getUserGuid())
//                .username(entity.get)
                .category(dto.projectEntity().getCategory())
                .title(dto.projectEntity().getTitle())
                .content(dto.projectEntity().getContent())
                .recruitmentTypeCd(dto.projectEntity().getRecuritmentTypeCd())
                .recruitmentStartDate(dto.projectEntity().getProgressStartDate())
                .recruitmentEndDate(dto.projectEntity().getRecuritmentEndDate())
                .progressTypeCd(dto.projectEntity().getProgressTypeCd())
//                .progressRegionCd(entity.getpro)
//                .progressPeriod(entity.getpro)
                .progressStartDate(dto.projectEntity().getProgressStartDate())
                .progressEndDate(dto.projectEntity().getProgressEndDate())
                .auditInfo(toAuditInfo(dto.projectEntity()))
                .projectSkill(dto.skillCds())
                .projectRequirement(dto.requirementEntities().stream().map(ProjectMapper::toRequirement).toList())
                .likeCount(dto.likeCount())
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
}
