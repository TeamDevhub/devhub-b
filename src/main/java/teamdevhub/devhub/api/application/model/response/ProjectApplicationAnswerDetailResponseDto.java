package teamdevhub.devhub.api.application.model.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationAnswerDetailResponseDto {

	private String applicationAnswerGuid;
	private String projectApplicationFormGuid;
	private String fileGuid;
	private String content;
	private String nickname;
	private String email;
	private String mannerDegree;
	private List<String> userSkillList;
	private String positionCd;
	private String introduction;
	private String aplyDate;

	public static ProjectApplicationAnswerDetailResponseDto fromDomain(ProjectApplicationAnswer answer) {
		return ProjectApplicationAnswerDetailResponseDto.builder()
			.applicationAnswerGuid(answer.getApplicationAnswerGuid())
			.projectApplicationFormGuid(answer.getProjectApplicationFormGuid())
			.fileGuid(answer.getFileGuid())
			.content(answer.getContent())
			.nickname(answer.getNickName())
			.email(answer.getEmail())
			.mannerDegree(String.valueOf(answer.getMannerDegree()))
			.userSkillList(answer.getUserSkillList())
			.positionCd(answer.getPositionCd())
			.introduction(answer.getIntroduction())
			.aplyDate(answer.getAplyDate())
			.build();
	}
}
