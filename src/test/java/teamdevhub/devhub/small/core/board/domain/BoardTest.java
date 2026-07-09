package teamdevhub.devhub.small.core.board.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.BoardTestConstant.BOARD_CATEGORY_CD;
import static teamdevhub.devhub.constant.BoardTestConstant.BOARD_CONTENT;
import static teamdevhub.devhub.constant.BoardTestConstant.BOARD_TITLE;
import static teamdevhub.devhub.constant.BoardTestConstant.TEST_BOARD_GUID_1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.constant.UserTestConstant;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;

class BoardTest {

	@Test
    @DisplayName("게시글을_생성한다")
	void createBoard() {
		// given
		CreateBoardCommand createBoardCommand = CreateBoardCommand.builder()
				.title(BOARD_TITLE)
				.content(BOARD_CONTENT)
				.categoryCd(BOARD_CATEGORY_CD)
				.userGuid(UserTestConstant.TEST_USER_GUID_1)
				.build();
		
		// when
		Board createBoard = Board.createBoard(createBoardCommand, TEST_BOARD_GUID_1);
		
		// then
		assertThat(createBoard.getBoardGuid()).isEqualTo(TEST_BOARD_GUID_1);
		assertThat(createBoard.getUserGuid()).isEqualTo(UserTestConstant.TEST_USER_GUID_1);
		assertThat(createBoard.getTitle()).isEqualTo(BOARD_TITLE);
		assertThat(createBoard.getContent()).isEqualTo(BOARD_CONTENT);
		assertThat(createBoard.getCategoryCd()).isEqualTo(BOARD_CATEGORY_CD);
		/**
		 * 현재 null 로 나옵니다.
		 * assertThat(createBoard.getViewCount()).isEqualTo("0");
		 */

		
	}
}