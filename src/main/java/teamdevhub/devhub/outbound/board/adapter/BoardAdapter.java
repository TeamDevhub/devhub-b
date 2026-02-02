package teamdevhub.devhub.outbound.board.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.BoardSummary;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.core.board.port.out.BoardRepository;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.outbound.board.persistence.BoardQueryRepository;

@Component
@RequiredArgsConstructor
public class BoardAdapter implements BoardRepository {
	private final BoardQueryRepository boardQueryRepository;
	
	@Override
	public PageResult<BoardSummary> boardList(SearchBoardCommand searchBoardCommand, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("registeredDate").descending());
		Page<BoardSummary> pageBoardList = boardQueryRepository.boardList(searchBoardCommand, pageable);
		
		return PageResult.of(
				pageBoardList.getContent(), 
				pageBoardList.getNumber(),
				pageBoardList.getSize(),
				pageBoardList.getTotalElements());
	}
}
