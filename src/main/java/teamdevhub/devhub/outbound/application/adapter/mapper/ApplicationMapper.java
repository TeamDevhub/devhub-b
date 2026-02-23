package teamdevhub.devhub.outbound.application.adapter.mapper;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationEntity;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;

import java.util.List;

public class ApplicationMapper {

	public static ProjectApplication toApplication(
		ProjectApplicationEntity applicationEntity,
		UserEntity applicantEntity,
		ProjectRequirementEntity requirementEntity,
		List<String> userSkillList
	) {
		return ProjectApplication.builder()
			.applicationGuid(applicationEntity.getApplicationGuid())
			.requirementGuid(applicationEntity.getRequirementGuid())
			.applicantGuid(applicationEntity.getApplicantGuid())
			.approverGuid(applicationEntity.getApproverGuid())
			.decisionDate(applicationEntity.getDecisionDate())
			.statusCd(applicationEntity.getStatusCd())
			.isCanceled(applicationEntity.isCanceled())
			.nickName(applicantEntity.getUsername())
			.email(applicantEntity.getEmail())
			.mannerDegree(applicantEntity.getMannerDegree())
			.userSkillList(userSkillList)
			.positionCd(requirementEntity.getPositionCd())
			.levelCd(requirementEntity.getLevelCd())
			.aplyDate(applicationEntity.getRegisteredDate() != null
				? applicationEntity.getRegisteredDate().toLocalDate().toString()
				: null)
			.auditInfo(AuditInfo.of(
				applicationEntity.getRegistrantGuid(),
				applicationEntity.getRegisteredDate(),
				applicationEntity.getModifierGuid(),
				applicationEntity.getModifiedDate()
			))
			.build();
	}
}
