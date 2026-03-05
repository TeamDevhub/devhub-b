package teamdevhub.devhub.core.board.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.core.board.port.in.usecase.BoardUseCase;
import teamdevhub.devhub.core.board.port.out.BoardRepository;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService implements BoardUseCase {
	
	private final IdentifierProvider identifierProvider;
	private final BoardRepository boardRepository;
	
	@Override
	public void createBoard(CreateBoardCommand createBoardCommand){
		String boardGuid = identifierProvider.generateIdentifier();
		Board board = Board.createBoard(createBoardCommand, boardGuid);
		boardRepository.save(board);
	}
}