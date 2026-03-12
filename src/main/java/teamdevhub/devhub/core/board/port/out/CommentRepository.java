package teamdevhub.devhub.core.board.port.out;

import java.util.List;
import java.util.Map;

import teamdevhub.devhub.core.board.domain.Comment;

public interface CommentRepository {

	Map<String, Long> countByCommentCount(List<String> boardGuids);

	List<Comment> findByBoardGuid(String boardGuid);

	void save(Comment comment);
}
