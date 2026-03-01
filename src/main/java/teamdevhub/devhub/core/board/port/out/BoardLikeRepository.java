package teamdevhub.devhub.core.board.port.out;

import java.util.List;
import java.util.Map;

public interface BoardLikeRepository {

	Map<String, Long> countByLikeCount(List<String> boardGuids);
}
