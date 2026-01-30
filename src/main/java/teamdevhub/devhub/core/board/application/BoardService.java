package teamdevhub.devhub.core.board.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.core.board.port.in.usecase.BoardUseCase;
import teamdevhub.devhub.core.board.port.out.BoardRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService implements BoardUseCase {
	
	private final BoardRepository boardRepository;
	
	@Override
	public PageResult<Board> boardList(SearchBoardCommand searchBoardCommand, PageCommand pageCommand){
		return boardRepository.boardList(searchBoardCommand, pageCommand.page(), pageCommand.size());
	}
}
