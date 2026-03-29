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
import teamdevhub.devhub.outbound.board.adapter.entity.BoardEntity;
import teamdevhub.devhub.outbound.board.adapter.mapper.BoardMapper;
import teamdevhub.devhub.outbound.board.persistence.JpaBoardRepository;

@Component
@RequiredArgsConstructor
public class BoardQueryAdapter implements BoardQueryRepository {
	private final JpaBoardRepository jpaBoardRepository;
	
	@Override
	public PageResult<Board> listBoard(SearchBoardCommand searchBoardCommand, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("registeredDate").descending().and(Sort.by("boardGuid")));
		Page<BoardEntity> pageBoardList = jpaBoardRepository.findByConditions(				
											searchBoardCommand.title(),
											searchBoardCommand.categoryCd(),
											searchBoardCommand.userGuid(),
											pageable);

		PageResult<Board> aa = PageResult.of(
				pageBoardList.getContent().stream().map(BoardMapper::toBoard).toList(), 
				pageBoardList.getNumber(),
				pageBoardList.getSize(),
				pageBoardList.getTotalElements());
		 return aa;
	} 
}
