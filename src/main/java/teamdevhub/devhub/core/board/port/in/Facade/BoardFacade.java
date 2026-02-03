package teamdevhub.devhub.core.board.port.in.Facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.core.board.port.in.usecase.BoardUseCase;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardFacade {

    private final BoardUseCase boardUseCase;

	public PageResult<Board> boardList(SearchBoardCommand searchBoardCommand, PageCommand pageCommand) {
		
		return boardUseCase.boardList(searchBoardCommand, pageCommand);
	}

}
