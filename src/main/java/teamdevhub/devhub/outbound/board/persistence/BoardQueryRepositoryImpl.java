package teamdevhub.devhub.outbound.board.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.BoardSummary;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.outbound.board.adapter.entity.BoardEntity;
import teamdevhub.devhub.outbound.board.adapter.entity.QBoardEntity;
import teamdevhub.devhub.outbound.board.adapter.entity.QBoardLikeEntity;
import teamdevhub.devhub.outbound.board.adapter.entity.QCommentEntity;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepositoryImpl implements BoardQueryRepository {

    private final JPAQueryFactory queryFactory;
    
	//정의
	public record BoardSummaryFlatDto(
			BoardEntity boardEntity,
			String likeCount,
			String commentCount
	) {}

    @Override
    public Page<BoardSummary> boardList(SearchBoardCommand SearchboardCommand, Pageable pageable) {
    	QBoardEntity boardEntity = QBoardEntity.boardEntity;    	
    	QBoardLikeEntity boardLikeEntity = QBoardLikeEntity.boardLikeEntity; 
    	QCommentEntity commentEntity = QCommentEntity.commentEntity; 
    	

    	//content
//    	 List<BoardSummary> content = queryFactory
//    	 		.select(Projections.constructor(
//    	 				BoardSummary.class,
//    	 				board.
//    	 				));
    	//total
    	 
    	return new PageImpl<>(content, pageable, total);
    }
}