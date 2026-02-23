package teamdevhub.devhub.core.application.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationAnswer {

	private String applicationAnswerGuid;
	private String applicationGuid;
	private String projectApplicationFormGuid;
	private String fileGuid;
	private String content;

	// 지원자 유저 정보
	private String nickName;
	private String email;
	private double mannerDegree;
	private List<String> userSkillList;
	private String positionCd;
	private String introduction;

	private String aplyDate;
}
