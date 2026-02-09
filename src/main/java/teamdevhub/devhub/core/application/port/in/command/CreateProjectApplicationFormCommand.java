package teamdevhub.devhub.core.application.port.in.command;

import lombok.Builder;

@Builder
public record CreateProjectApplicationFormCommand(String projectGuid, String applicationFormGuid) {

}
