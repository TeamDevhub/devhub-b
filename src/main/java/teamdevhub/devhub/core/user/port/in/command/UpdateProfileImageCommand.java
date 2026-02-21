package teamdevhub.devhub.core.user.port.in.command;

import lombok.Builder;

@Builder
public record UpdateProfileImageCommand(String userGuid, String fileGuid) {}

