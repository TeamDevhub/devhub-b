package teamdevhub.devhub.outbound.board.adapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.port.out.BoardLikeRepository;
import teamdevhub.devhub.outbound.board.persistence.JpaBoardLikeRepository;

@Component
@RequiredArgsConstructor
public class BoardLikeAdapter implements BoardLikeRepository {
	
	private final JpaBoardLikeRepository jpaBoardLikeRepository;
	
	@Override
	public Map<String, Long> countByLikeCount(List<String> boardGuids) {
		return jpaBoardLikeRepository.countByLikeCount(boardGuids)
				.stream()
				.collect(Collectors.toMap(
						row -> (String) row[0],
						row -> (Long) row[1]
						));			
	}
}
