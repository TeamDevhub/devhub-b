package teamdevhub.devhub.core.board.port.in.command;

import lombok.Builder;

@Builder
public record UpdateBoardCommand(String title, String categoryCd, String content, String userGuid, String boardGuid) {}