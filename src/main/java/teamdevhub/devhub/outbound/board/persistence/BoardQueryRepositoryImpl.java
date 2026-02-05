package teamdevhub.devhub.outbound.board.persistence;

import static teamdevhub.devhub.outbound.board.adapter.entity.QBoardEntity.boardEntity;
import static teamdevhub.devhub.outbound.board.adapter.entity.QBoardLikeEntity.boardLikeEntity;
import static teamdevhub.devhub.outbound.board.adapter.entity.QCommentEntity.commentEntity;
import static teamdevhub.devhub.outbound.user.adapter.entity.QUserEntity.userEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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

    @Override
    public Page<Board> boardList(SearchBoardCommand SearchboardCommand, Pageable pageable) {	
    	
    	JPAQuery<?> commonQuery = queryFactory
    			.from(boardEntity)
    			.where(
    					titleCondition(SearchboardCommand.title()),
    					categoryCdCondition(SearchboardCommand.categoryCd())
    					);
    	
    	List<String> boardGuidList = commonQuery
                .select(boardEntity.boardGuid)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();
    	
    	JPQLQuery<Long> likeCountSubQuery = JPAExpressions
    			.select(boardLikeEntity.count())
				.from(boardLikeEntity)
				.where(boardLikeEntity.boardGuid.eq(boardEntity.boardGuid));
    	
    	JPQLQuery<Long> commentCountSubQuery = JPAExpressions
    			.select(commentEntity.count())
				.from(commentEntity)
				.where(commentEntity.boardGuid.eq(boardEntity.boardGuid));
    	
    	JPQLQuery<String> userNameSubQuery = JPAExpressions
    			.select(userEntity.username)
				.from(userEntity)
				.where(userEntity.userGuid.eq(boardEntity.userGuid));
    	
    	List<Tuple> boards = queryFactory
                .select(boardEntity,
                        likeCountSubQuery,
                        commentCountSubQuery,
                        userNameSubQuery
                        )
                .from(boardEntity)
                .where(boardEntity.boardGuid.in(boardGuidList))
                .fetch();
    	
    	Long total = Optional.ofNullable(commonQuery
 		       .select(boardEntity.countDistinct())
 		       .fetchOne()
 			).orElse(0L);    
    	
    	List<Board> content = boards.stream().map(tuple -> {
    		BoardEntity board = tuple.get(boardEntity);
    		return BoardMapper.toBoard(
    				Objects.requireNonNull(board),
    				Objects.requireNonNull(tuple.get(likeCountSubQuery)).toString(),
    				Objects.requireNonNull(tuple.get(commentCountSubQuery)).toString(),
    				Objects.requireNonNull(tuple.get(userNameSubQuery)).toString()
    				);
    	}).toList();

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