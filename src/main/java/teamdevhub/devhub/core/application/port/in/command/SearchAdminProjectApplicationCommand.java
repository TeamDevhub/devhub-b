package teamdevhub.devhub.core.application.port.in.command;

import lombok.Builder;

@Builder
public record SearchAdminProjectApplicationCommand(String projectGuid, String approvalStatusCd, String positionCd, String levelCd) {

}
