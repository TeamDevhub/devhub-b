package teamdevhub.devhub.small.core.board.port.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.BoardTestConstant.BOARD_CATEGORY_CD;
import static teamdevhub.devhub.constant.BoardTestConstant.BOARD_CONTENT;
import static teamdevhub.devhub.constant.BoardTestConstant.BOARD_TITLE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.constant.UserTestConstant;
import teamdevhub.devhub.core.board.port.in.Facade.BoardFacade;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.board.FakeBoardQueryUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.board.FakeBoardUseCase;

public class BoardFacadeTest {
	
	private BoardFacade boardFacade;
	
	private FakeBoardUseCase boardUseCase;
	private FakeBoardQueryUseCase boardQueryUseCase;
	
	@BeforeEach
	void init() {
		boardUseCase = new FakeBoardUseCase();
		
		boardFacade = new BoardFacade(boardQueryUseCase, boardUseCase);
	}
	
	@Test
	@DisplayName("게시글_생성_요청이_전달된다")
	void createBoard() {
		// given
		CreateBoardCommand createBoardCommand = new CreateBoardCommand(
				BOARD_TITLE,
				BOARD_CONTENT,
				BOARD_CATEGORY_CD,
				UserTestConstant.TEST_USER_GUID_1
		);
		
		// when
		boardFacade.createBoard(createBoardCommand);
		
		// then
		assertThat(boardUseCase.isCalled()).isTrue();
	}
}