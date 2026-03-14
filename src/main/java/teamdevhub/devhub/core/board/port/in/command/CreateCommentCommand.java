package teamdevhub.devhub.core.board.port.in.command;

import lombok.Builder;

@Builder
public record CreateCommentCommand(String boardGuid, String content, String userGuid) {}