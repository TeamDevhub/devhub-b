package teamdevhub.devhub.outbound.board.adapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.port.out.CommentRepository;
import teamdevhub.devhub.outbound.board.persistence.JpaCommentRepository;

@Component
@RequiredArgsConstructor
public class CommentAdapter implements CommentRepository {
	
	private final JpaCommentRepository jpaCommentRepository;
	
	@Override
	public Map<String, Long> countByCommentCount(List<String> boardGuids) {
		return jpaCommentRepository.countByCommentCount(boardGuids)
				.stream()
				.collect(Collectors.toMap(
						row -> (String) row[0],
						row -> (Long) row[1]
						));			
	}
}
