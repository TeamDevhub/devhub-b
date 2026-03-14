package teamdevhub.devhub.outbound.board.adapter;


import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.out.BoardRepository;
import teamdevhub.devhub.outbound.board.adapter.entity.BoardEntity;
import teamdevhub.devhub.outbound.board.adapter.mapper.BoardMapper;
import teamdevhub.devhub.outbound.board.persistence.JpaBoardRepository;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Component
@RequiredArgsConstructor
public class BoardAdapter implements BoardRepository {
	private final JpaBoardRepository jpaBoardRepository;
	
	@Override
	public void save(Board board) {
		jpaBoardRepository.save(BoardMapper.toEntity(board));
	}
	
	@Override
	public Board detailBoard(String boardGuid) {
		BoardEntity boardEntity = jpaBoardRepository.findByBoardGuid(boardGuid)
				.orElseThrow(() -> AdapterDataException.of(ErrorCode.READ_FAIL));
		
		return BoardMapper.toDomain(boardEntity);
	}
	
	@Override
	public void updateBoard(Board board) {
		jpaBoardRepository.save(BoardMapper.toEntity(board));
	}
	
	@Override
	public Board findByBoardGuid(String boardGuid) {
		BoardEntity boardEntity = jpaBoardRepository.findByBoardGuid(boardGuid)
				.orElseThrow(() -> AdapterDataException.of(ErrorCode.READ_FAIL));
		
		return BoardMapper.toDomain(boardEntity);
	}
	
	@Override
	public void updateViewCount(String boardGuid) {
		jpaBoardRepository.updateViewCount(boardGuid);
	}
}
