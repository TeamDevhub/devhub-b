package teamdevhub.devhub.core.board.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;


@Getter
public class BoardSummary {
	
	private final String boardGuid;
	private final String userGuid;
	private final String categoryCd;
	private final String title;
	private final String content;
	private final String viewCount;
	private final String likeCount;
	private final String commentCount;
	
	private final AuditInfo auditInfo;
	
	@Builder
	private BoardSummary (
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
