package teamdevhub.devhub.core.board.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.board.domain.Comment;
import teamdevhub.devhub.core.board.port.in.command.CreateCommentCommand;
import teamdevhub.devhub.core.board.port.in.command.UpdateCommentCommand;

public interface CommentUseCase {

	List<Comment> commentList(String boardGuid);

	void createComment(CreateCommentCommand createCommentCommand);

	void deleteComment(String boardGuid, String commentGuid);

	void updateComment(UpdateCommentCommand updateCommentCommand);
}