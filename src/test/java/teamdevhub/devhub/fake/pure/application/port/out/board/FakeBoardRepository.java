package teamdevhub.devhub.fake.pure.application.port.out.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.out.BoardRepository;

public class FakeBoardRepository implements BoardRepository {
	
	private final Map<String, Board> store = new HashMap<>();
    private final List<String> calledMethods = new ArrayList<>();

	
	@Override
	public void save(Board board) {
		calledMethods.add("save");
		store.put(board.getBoardGuid(), board);
	}
	
	public boolean wasCalled(String methodName) {
        return calledMethods.contains(methodName);
    }

}
