package teamdevhub.devhub.core.board.port.in.usecase;

import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.core.board.port.in.command.UpdateBoardCommand;

public interface BoardUseCase {

	void createBoard(CreateBoardCommand createBoardCommand);

	Board detailBoard(String boardGuid, Boolean cookieResult);

	void updateBoard(UpdateBoardCommand updateBoardCommand);

	void likeBoard(String userGuid, String boardGuid);

	void deleteBoard(String boardGuid);
}