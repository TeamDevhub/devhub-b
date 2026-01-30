package teamdevhub.devhub.core.board.port.out;

import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface BoardRepository {

	PageResult<Board> boardList(SearchBoardCommand searchBoardCommand, int page, int size);
}
