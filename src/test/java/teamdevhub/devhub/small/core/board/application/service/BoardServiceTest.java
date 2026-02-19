package teamdevhub.devhub.small.core.board.application.service;

import org.junit.jupiter.api.BeforeEach;

import teamdevhub.devhub.core.board.application.BoardService;

public class BoardServiceTest {
	
	private BoardService boardService;
	private FakeBoardRepository boardRepository;
	
	@BeforeEach
	void init() {
		boardRepository = new FakeBoardRepository();
		
		boardService = new boardService(boardRepository);
	}
}
