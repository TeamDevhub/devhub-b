package teamdevhub.devhub.core.board.domain;

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
	@Builder.Default
	private String viewCount = "0";
	
	private final String userName;
	private String likeCount;
	private String commentCount;
	
	private final AuditInfo auditInfo;
	
//	@Builder
//	private Board(
//			String boardGuid,
//			String userGuid,
//			String categoryCd,
//			String title,
//			String content,
//			String viewCount,
//			String likeCount,
//			String commentCount,
//			String userName,
//			AuditInfo auditInfo
//	) {
//		this.boardGuid = boardGuid;
//		this.userGuid = userGuid;
//		this.categoryCd = categoryCd;
//		this.title = title;
//		this.content = content;
//		this.viewCount = viewCount;
//		this.likeCount = likeCount;
//		this.commentCount = commentCount;
//		this.userName = userName;
//		
//		if (auditInfo == null) {
//            this.auditInfo = AuditInfo.empty();
//        } else {
//            this.auditInfo = auditInfo;
//        }
//	}

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
}
