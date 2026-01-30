package teamdevhub.devhub.outbound.board.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.outbound.board.adapter.entity.BoardEntity;

public interface BoardQueryRepository {

    Page<BoardEntity> listUser(SearchBoardCommand searchBoardCommand, Pageable pageable);
}
