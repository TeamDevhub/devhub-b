package teamdevhub.devhub.core.board.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.board.domain.Comment;
import teamdevhub.devhub.core.board.port.in.command.CreateCommentCommand;

public interface CommentUseCase {

	List<Comment> commentList(String boardGuid);

	void createComment(CreateCommentCommand createCommentCommand);
}