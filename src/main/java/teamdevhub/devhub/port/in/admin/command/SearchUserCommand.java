package teamdevhub.devhub.port.in.admin.command;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchUserCommand(Boolean blocked, LocalDateTime joinedFrom, LocalDateTime joinedTo, String keyword) {}
