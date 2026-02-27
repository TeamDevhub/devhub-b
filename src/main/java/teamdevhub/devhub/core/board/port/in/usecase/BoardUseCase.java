package teamdevhub.devhub.core.board.port.in.usecase;

import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;

public interface BoardUseCase {

	void createBoard(CreateBoardCommand createBoardCommand);

	Board detailBoard(String boardGuid);
}