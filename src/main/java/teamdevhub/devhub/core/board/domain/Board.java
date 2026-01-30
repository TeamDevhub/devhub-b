package teamdevhub.devhub.core.board.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;


@Getter
public class Board {
	
	private final String boardGuid;
	private final String userGuid;
	private String categoryCd;
	private String title;
	private String content;
	private String viewCount;
	private String likeCount;
	private String commentCount;
	
	private final AuditInfo auditInfo;
	
	@Builder
	private Board(
			String boardGuid,
			String userGuid,
			String categoryCd,
			String title,
			String content,
			String viewCount,
			String likeCount,
			String commentCount,
			AuditInfo auditInfo
	) {
		this.boardGuid = boardGuid;
		this.userGuid = userGuid;
		this.categoryCd = categoryCd;
		this.title = title;
		this.content = content;
		this.viewCount = viewCount;
		this.likeCount = likeCount;
		this.commentCount = commentCount;
		
		if (auditInfo == null) {
            this.auditInfo = AuditInfo.empty();
        } else {
            this.auditInfo = auditInfo;
        }
	}
}
