package teamdevhub.devhub.outbound.board.persistence;

import static teamdevhub.devhub.outbound.board.adapter.entity.QBoardEntity.boardEntity;
import static teamdevhub.devhub.outbound.board.adapter.entity.QBoardLikeEntity.boardLikeEntity;
import static teamdevhub.devhub.outbound.board.adapter.entity.QCommentEntity.commentEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.outbound.board.adapter.entity.BoardEntity;
import teamdevhub.devhub.outbound.board.adapter.mapper.BoardMapper; 

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
    public Page<Board> boardList(SearchBoardCommand SearchboardCommand, Pageable pageable) {	
    	//공통 베이스
    	JPAQuery<?> commonQuery = queryFactory
    			.from(boardEntity)
    			.leftJoin(boardLikeEntity).on(boardEntity.boardGuid.eq(boardLikeEntity.boardGuid))
    			.leftJoin(commentEntity).on(boardEntity.boardGuid.eq(commentEntity.boardGuid))
    			.where(
    					titleCondition(SearchboardCommand.title()),
    					categoryCdCondition(SearchboardCommand.categoryCd())
    					);

    	//content
    	//map(board::toboardsummary)를 안하는 이유가 있음
    	List<BoardSummaryFlatDto> flatList = commonQuery
    			.select(Projections.constructor(
	    					BoardSummaryFlatDto.class,
	    					boardEntity,
	    					boardLikeEntity.boardGuid.countDistinct().stringValue(),
	    					commentEntity.boardGuid.countDistinct().stringValue()
    					))
    			.groupBy(boardEntity.boardGuid)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    	
    	List<Board> content = flatList
    			.stream()
    			.map(flat -> {
    				Board board = BoardMapper.toDomain(flat.boardEntity());
    				board.summary(flat.likeCount(), flat.commentCount());
    				return board;
    			})
                .toList();
    	
    	//total
    	Long total = Optional.ofNullable(commonQuery
    		       .select(boardEntity.boardGuid.countDistinct())
    		       .fetchOne()
    			).orElse(0L);    
    	 
    	return new PageImpl<>(content, pageable, total);
    }

	private BooleanExpression titleCondition(String title) {
		 if (title == null) {
	            return null;
	     }
		return boardEntity.title.containsIgnoreCase(title);
	}
	
	private BooleanExpression categoryCdCondition(String categoryCd) {
		 if (categoryCd == null) {
	            return null;
	     }
		return boardEntity.categoryCd.containsIgnoreCase(categoryCd);
	}
}