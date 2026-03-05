package teamdevhub.devhub.small.core.board.port.command;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.BoardTestConstant.BOARD_CATEGORY_CD;
import static teamdevhub.devhub.constant.BoardTestConstant.BOARD_CONTENT;
import static teamdevhub.devhub.constant.BoardTestConstant.BOARD_TITLE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.constant.UserTestConstant;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;

class CreateBoardCommandTest {
	
	
	@Test
	@DisplayName("게시글_생성_커맨드_검증")
	void createRequestDtoToCommand() {
		// given, when
		CreateBoardCommand createBoardCommand = CreateBoardCommand.builder()
				.userGuid(UserTestConstant.TEST_USER_GUID_1)
				.title(BOARD_TITLE)
				.content(BOARD_CONTENT)
				.categoryCd(BOARD_CATEGORY_CD)
				.build();
		
		//then
		assertThat(createBoardCommand.userGuid()).isEqualTo(UserTestConstant.TEST_USER_GUID_1);
		assertThat(createBoardCommand.title()).isEqualTo(BOARD_TITLE);
		assertThat(createBoardCommand.content()).isEqualTo(BOARD_CONTENT);
		assertThat(createBoardCommand.categoryCd()).isEqualTo(BOARD_CATEGORY_CD);
	}

}