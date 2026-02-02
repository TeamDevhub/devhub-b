package teamdevhub.devhub.outbound.board.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import teamdevhub.devhub.core.board.domain.BoardSummary;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;

public interface BoardQueryRepository {

	Page<BoardSummary> boardList(SearchBoardCommand searchBoardCommand, Pageable pageable);
}
