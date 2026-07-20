package teamdevhub.devhub.core.application.port.in.command;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.ProjectApprovalStatus;

@Builder
public record ApproveApplicationCommand(
	String applicationGuid,
	String approverGuid,
	boolean approved
) {
	public String resolveStatusCd() {
		return approved ? ProjectApprovalStatus.APPROVED.getCode() : ProjectApprovalStatus.REJECTED.getCode();
	}
}
