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
	
    public Board findByBoardGuid(String boardGuid) {
        return store.get(boardGuid);
    }
}