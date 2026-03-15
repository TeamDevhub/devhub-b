package teamdevhub.devhub.shared.enums;

import lombok.Getter;

@Getter
public enum ProjectApprovalStatus {

	PENDING("001"),
    APPROVED("002"),
    REJECTED("003");

	private final String code;

	ProjectApprovalStatus(String code) {
		this.code = code;
	}

}
