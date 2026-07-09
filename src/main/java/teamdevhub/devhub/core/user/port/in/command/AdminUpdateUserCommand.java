package teamdevhub.devhub.core.user.port.in.command;

import lombok.Builder;

@Builder
public record AdminUpdateUserCommand(
        String userGuid,
        String username,
        String introduction
) {}
