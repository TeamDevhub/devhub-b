package teamdevhub.devhub.outbound.board.adapter.mapper;

import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.outbound.board.adapter.entity.BoardEntity;

public class BoardMapper {
	public static BoardEntity toEntity(Board board) {
		return BoardEntity.builder()
				.boardGuid(board.getBoardGuid())
				.userGuid(board.getUserGuid())
				.categoryCd(board.getCategoryCd())
				.title(board.getTitle())
				.content(board.getContent())
				.viewCount(board.getViewCount())
				.build();
	}
	
	public static Board toDomain(BoardEntity boardEntity) {
		return Board.of(
				boardEntity.getBoardGuid(),
				boardEntity.getUserGuid(),
				boardEntity.getCategoryCd(),
				boardEntity.getTitle(),
				boardEntity.getContent(),
				boardEntity.getViewCount(),
				toAuditInfo(boardEntity)
		);
	}
	
	private static AuditInfo toAuditInfo(BoardEntity boardEntity) {
		  return AuditInfo.of(
				  boardEntity.getRegistrantGuid(),
				  boardEntity.getRegisteredDate(),
				  boardEntity.getModifierGuid(),
				  boardEntity.getModifiedDate()
	        );
	}

}
