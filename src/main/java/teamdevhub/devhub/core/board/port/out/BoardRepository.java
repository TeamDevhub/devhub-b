package teamdevhub.devhub.core.board.port.out;

import teamdevhub.devhub.core.board.domain.Board;

public interface BoardRepository {

	void save(Board board);

	Board detailBoard(String boardGuid);

	void updateBoard(Board board);

	Board findByBoardGuid(String boardGuid);
}
