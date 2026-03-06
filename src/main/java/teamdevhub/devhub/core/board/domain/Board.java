package teamdevhub.devhub.core.board.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.core.common.audit.AuditInfo;


@Getter
@Builder
public class Board {
	
	private final String boardGuid;
	private final String userGuid;
	private String categoryCd;
	private String title;
	private String content;
	private String viewCount;
	
	private String userName;
	private String likeCount;
	private String commentCount;
	
	private List<Comment> commentList;
	private String userEmail;

	private final AuditInfo auditInfo;

	public static Board of(
			String boardGuid, 
			String userGuid, 
			String categoryCd, 
			String title,
			String content,
			String viewCount, 
			AuditInfo auditInfo
	) {
		return Board.builder()
				.boardGuid(boardGuid)
				.userGuid(userGuid)
				.categoryCd(categoryCd)
				.title(title)
				.content(content)
				.viewCount(viewCount)
				.auditInfo(auditInfo)
                .build();
	}

	public static Board createBoard(CreateBoardCommand createBoardCommand, String boardGuid) {
		return Board.builder()
				.boardGuid(boardGuid)
				.userGuid(createBoardCommand.userGuid())
				.categoryCd(createBoardCommand.categoryCd())
				.title(createBoardCommand.title())
				.content(createBoardCommand.content())
				.build();
	}
	
	public void fillSummarySubquery(String likeCount, String commentCount, String userName) {
		 this.likeCount = likeCount;
		 this.commentCount = commentCount;
		 this.userName = userName;

	}
	
	public void fillDetailSubquery(String likeCount, String commentCount, String userName, String userEmail, List<Comment> commentList) {
		 this.likeCount = likeCount;
		 this.commentCount = commentCount;
		 this.userName = userName;
		 this.userEmail = userEmail;
		 this.commentList = commentList;
	}
}
