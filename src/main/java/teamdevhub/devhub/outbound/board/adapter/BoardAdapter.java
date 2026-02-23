package teamdevhub.devhub.outbound.board.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.out.BoardRepository;
import teamdevhub.devhub.outbound.board.adapter.mapper.BoardMapper;
import teamdevhub.devhub.outbound.board.persistence.JpaBoardRepository;

@Component
@RequiredArgsConstructor
public class BoardAdapter implements BoardRepository {
	private final JpaBoardRepository jpaBoardRepository;
	
	@Override
	public void save(Board board) {
		jpaBoardRepository.save(BoardMapper.toEntity(board));
	}
}
