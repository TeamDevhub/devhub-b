package teamdevhub.devhub.core.board.port.out;

import java.util.List;
import java.util.Map;

import teamdevhub.devhub.core.board.domain.BoardLike;

public interface BoardLikeRepository {

	Map<String, Long> countByLikeCount(List<String> boardGuids);
	
	BoardLike likeBoard(String boardGuid, String userGuid);

	void deleteBoardLike(BoardLike boardLike);

	void save(BoardLike boardLike);

	boolean existsByBoardGuidAndUserGuid(String boardGuid, String userGuid);

}
