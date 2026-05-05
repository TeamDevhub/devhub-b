package teamdevhub.devhub.shared.enums;

import lombok.Getter;

@Getter
public enum ProjectApprovalStatus {

	PENDING("3301"),
    APPROVED("3302"),
    REJECTED("3303");

	private final String code;

	ProjectApprovalStatus(String code) {
		this.code = code;
	}

}
