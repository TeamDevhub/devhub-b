package teamdevhub.devhub.core.board.port.out;

import teamdevhub.devhub.core.board.domain.Board;

public interface BoardRepository {

	void save(Board board);
}
