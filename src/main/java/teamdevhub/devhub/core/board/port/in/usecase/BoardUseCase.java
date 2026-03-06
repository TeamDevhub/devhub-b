package teamdevhub.devhub.core.board.port.in.usecase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.core.board.port.in.command.UpdateBoardCommand;

public interface BoardUseCase {

	void createBoard(CreateBoardCommand createBoardCommand);

	Board detailBoard(String boardGuid, HttpServletRequest request, HttpServletResponse response);

	void updateBoard(UpdateBoardCommand updateBoardCommand);
}