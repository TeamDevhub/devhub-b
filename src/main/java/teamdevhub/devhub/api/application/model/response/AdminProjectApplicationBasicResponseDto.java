package teamdevhub.devhub.api.application.model.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.application.domain.ProjectApplication;

@Getter
@Builder
public class AdminProjectApplicationBasicResponseDto {
	
	private String applicantGuid;
	private String userGuid;
	private String email;
	private String positionCd;
	private String levelCd;
	private String approvalStatusCd;
	
	public static AdminProjectApplicationBasicResponseDto fromDomain(ProjectApplication application) {
		return AdminProjectApplicationBasicResponseDto.builder()
				.applicantGuid(application.getApplicantGuid())
				.userGuid(application.getUserName())
				.email(application.getEmail())
				.positionCd(application.getPositionCd())
				.levelCd(application.getLevelCd())
				.approvalStatusCd(application.getStatusCd())
				.build();
	}

}
