package teamdevhub.devhub.api.application.model.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.application.domain.ProjectApplication;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationDetailResponseDto {

	private String applicationGuid;
	private String requirementGuid;
	private String applicantGuid;
	private String approverGuid;
	private String decisionDate;
	private String statusCd;
	private String nickName;
	private String email;
	private String mannerDegree;
	private List<String> userSkillList;
	private String positionCd;
	private String levelCd;
	private String aplyDate;

	public static ProjectApplicationDetailResponseDto fromDomain(ProjectApplication application) {
		return ProjectApplicationDetailResponseDto.builder()
			.applicationGuid(application.getApplicationGuid())
			.requirementGuid(application.getRequirementGuid())
			.applicantGuid(application.getApplicantGuid())
			.approverGuid(application.getApproverGuid())
			.decisionDate(application.getDecisionDate())
			.statusCd(application.getStatusCd())
			.nickName(application.getNickName())
			.email(application.getEmail())
			.mannerDegree(String.valueOf(application.getMannerDegree()))
			.userSkillList(application.getUserSkillList())
			.positionCd(application.getPositionCd())
			.levelCd(application.getLevelCd())
			.aplyDate(application.getApplyDate())
			.build();
	}
}
