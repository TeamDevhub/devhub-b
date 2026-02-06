package teamdevhub.devhub.core.board.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.core.board.port.in.usecase.BoardQueryUseCase;
import teamdevhub.devhub.core.board.port.out.BoardQueryRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardQueryService implements BoardQueryUseCase {
	
	private final BoardQueryRepository boardQueryRepository;
	
	@Override
	public PageResult<Board> listBoard(SearchBoardCommand searchBoardCommand, PageCommand pageCommand){
		return boardQueryRepository.listBoard(searchBoardCommand, pageCommand.page(), pageCommand.size());
	}
}
