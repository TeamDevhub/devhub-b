package teamdevhub.devhub.outbound.board.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.core.board.port.out.BoardQueryRepository;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.outbound.board.persistence.BoardQueryDao;

@Component
@RequiredArgsConstructor
public class BoardQueryAdapter implements BoardQueryRepository {
	private final BoardQueryDao boardQueryDao;
	
	@Override
	public PageResult<Board> listBoard(SearchBoardCommand searchBoardCommand, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("registeredDate").descending());
		Page<Board> pageBoardList = boardQueryDao.listBoard(searchBoardCommand, pageable);
		
		return PageResult.of(
				pageBoardList.getContent(), 
				pageBoardList.getNumber(),
				pageBoardList.getSize(),
				pageBoardList.getTotalElements());
	}
}
