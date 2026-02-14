package teamdevhub.devhub.core.project.port.in.command;

import lombok.Builder;

@Builder
public record CreateProjectRequirementCommand(String position, String level, int capacity) {}
