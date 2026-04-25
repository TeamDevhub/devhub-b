package teamdevhub.devhub.core.board.port.out;

import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.SearchAdminBoardCommand;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface BoardQueryRepository {

	PageResult<Board> listBoard(SearchBoardCommand searchBoardCommand, int page, int size);

	PageResult<Board> listAdminBoard(SearchAdminBoardCommand searchAdminBoardCommand, int page, int size);
}
