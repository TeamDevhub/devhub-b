package teamdevhub.devhub.core.board.port.in.command;

import lombok.Builder;

@Builder
public record SearchBoardCommand(String title, String categoryCd, String userGuid) {}
