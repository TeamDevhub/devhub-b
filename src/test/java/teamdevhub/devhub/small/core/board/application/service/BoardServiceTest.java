package teamdevhub.devhub.small.core.board.application.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.constant.UserTestConstant;
import teamdevhub.devhub.core.board.application.BoardService;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.fake.pure.application.port.out.board.FakeBoardRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.BoardTestConstant.*;

public class BoardServiceTest {
	
	private BoardService boardService;
	private FakeBoardRepository boardRepository;
	
	@BeforeEach
	void init() {
		boardRepository = new FakeBoardRepository();
        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_BOARD_GUID_1);

		boardService = new BoardService(
				fakeUuidIdentifierProvider, 
				boardRepository
		);
	}
	
	@Test
	@DisplayName("게시글을_생성한다")
	void createBoard() {
		//given
        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_BOARD_GUID_1);
    	boardService = new BoardService(
				fakeUuidIdentifierProvider, 
				boardRepository
		);
    	
    	//when
		CreateBoardCommand createBoardCommand = new CreateBoardCommand(
				BOARD_TITLE,
				BOARD_CONTENT,
				BOARD_CATEGORY_CD,
				UserTestConstant.TEST_USER_GUID_1
				);
		boardService.createBoard(createBoardCommand);
		
		//then
		assertThat(boardRepository.wasCalled("save")).isTrue();
		
		
	}
}
