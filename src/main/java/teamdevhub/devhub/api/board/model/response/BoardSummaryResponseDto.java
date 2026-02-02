package teamdevhub.devhub.api.board.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.board.domain.BoardSummary;

@Getter
@SuperBuilder
@NoArgsConstructor
public class BoardSummaryResponseDto extends BoardBasicResponseDto{

	private String likeCount;
	private String commentCount;
    
    public static BoardSummaryResponseDto fromDomain(BoardSummary boardSummary) {
    	return BoardSummaryResponseDto.builder()
    			.boardGuid(boardSummary.getBoardGuid())
    			.userGuid(boardSummary.getUserGuid())
    			.categoryCd(boardSummary.getCategoryCd())
    			.title(boardSummary.getTitle())
    			.content(boardSummary.getContent())
    			.viewCount(boardSummary.getViewCount())
    			.likeCount(boardSummary.getLikeCount())
    			.commentCount(boardSummary.getCommentCount())
    			.registrantGuid(boardSummary.getAuditInfo().registrantGuid())
                .registeredDate(boardSummary.getAuditInfo().registeredDate())
                .modifierGuid(boardSummary.getAuditInfo().modifierGuid())
                .modifiedDate(boardSummary.getAuditInfo().modifiedDate())
                .build();
    		
    }
}
