package teamdevhub.devhub.core.board.port.in.usecase;

import teamdevhub.devhub.core.board.domain.BoardSummary;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface BoardUseCase {

	PageResult<BoardSummary> boardList(SearchBoardCommand searchBoardCommand, PageCommand pageCommand);
}
