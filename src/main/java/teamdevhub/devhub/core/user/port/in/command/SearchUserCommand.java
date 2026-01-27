package teamdevhub.devhub.core.user.port.in.command;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchUserCommand(Boolean blocked, LocalDateTime joinedFrom, LocalDateTime joinedTo, String keyword) {}
