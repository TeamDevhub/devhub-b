package teamdevhub.devhub.core.application.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;

import java.util.List;

@Getter
@Builder
public class ProjectApplication {

	private String applicationGuid;
	private String requirementGuid;
	private String applicantGuid;
	private String approverGuid;
	private String decisionDate;
	private String statusCd;
	private boolean isCanceled;

	// 지원자 유저 정보
	private String nickName;
	private String email;
	private double mannerDegree;
	private List<String> userSkillList;

	// 모집 요건 정보
	private String positionCd;
	private String levelCd;

	/**
	 * 리뷰
	 * 오탈자 수정했습니다.(aply-> apply)
	 */
	private String applyDate;

	private AuditInfo auditInfo;
}
