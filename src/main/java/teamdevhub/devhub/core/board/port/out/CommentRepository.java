package teamdevhub.devhub.core.board.port.out;

import java.util.List;
import java.util.Map;

public interface CommentRepository {

	Map<String, Long> countByCommentCount(List<String> boardGuids);
}
