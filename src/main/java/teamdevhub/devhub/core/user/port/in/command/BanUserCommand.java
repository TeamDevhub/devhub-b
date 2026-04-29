package teamdevhub.devhub.core.user.port.in.command;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BanUserCommand(
        String userGuid,
        LocalDateTime blockEndDate
) {}
