package teamdevhub.devhub.core.application.port.in.command;

import lombok.Builder;

@Builder
public record SearchApplicationFormCommand(String title, Boolean isUsed, Boolean isCustomized) {

}
