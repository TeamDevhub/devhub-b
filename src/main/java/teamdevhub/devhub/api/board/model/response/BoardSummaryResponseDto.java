package teamdevhub.devhub.api.board.model.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.board.domain.Board;

@Getter
@SuperBuilder
@NoArgsConstructor
public class BoardSummaryResponseDto extends BoardBasicResponseDto{

	private String likeCount;
	private String commentCount;
    
    public static BoardSummaryResponseDto fromDomain(Board board) {
    	return BoardSummaryResponseDto.builder()
    			.boardGuid(board.getBoardGuid())
    			.userGuid(board.getUserGuid())
    			.categoryCd(board.getCategoryCd())
    			.title(board.getTitle())
    			.content(board.getContent())
    			.viewCount(board.getViewCount())
    			.likeCount(board.getLikeCount())
    			.commentCount(board.getCommentCount())
    			.registrantGuid(board.getAuditInfo().registrantGuid())
                .registeredDate(board.getAuditInfo().registeredDate())
                .modifierGuid(board.getAuditInfo().modifierGuid())
                .modifiedDate(board.getAuditInfo().modifiedDate())
                .build();
    		
    }
}
