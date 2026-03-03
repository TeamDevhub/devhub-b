package teamdevhub.devhub.core.application.port.in.command;

import lombok.Builder;

@Builder
public record ApproveApplicationCommand(
	String applicationGuid,
	String approverGuid,
	boolean approved
) {
	private static final String STATUS_APPROVED = "002";
	private static final String STATUS_REJECTED = "003";

	public String resolveStatusCd() {
		return approved ? STATUS_APPROVED : STATUS_REJECTED;
	}
}
