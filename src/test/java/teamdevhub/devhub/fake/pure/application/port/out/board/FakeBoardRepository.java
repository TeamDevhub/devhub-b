package teamdevhub.devhub.fake.pure.application.port.out.board;

import java.util.HashMap;
import java.util.Map;

import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.out.BoardRepository;

public class FakeBoardRepository implements BoardRepository {
	
	private final Map<String, Board> store = new HashMap<>();
	
	@Override
	public void save(Board board) {
		store.put(board.getBoardGuid(), board);
	}

	@Override
	public Board detailBoard(String boardGuid) {
		return null;
	}

	@Override
	public void updateBoard(Board board) {

	}

	public Board findByBoardGuid(String boardGuid) {
        return store.get(boardGuid);
    }

	@Override
	public void updateViewCount(String boardGuid) {

	}

	@Override
	public void deleteBoard(String boardGuid) {

	}
}