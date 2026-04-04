package teamdevhub.devhub.fake.pure.application.port.in.usecase.board;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.core.board.port.in.command.UpdateBoardCommand;
import teamdevhub.devhub.core.board.port.in.usecase.BoardUseCase;

import static teamdevhub.devhub.constant.BoardTestConstant.*;

public class FakeBoardUseCase implements BoardUseCase{
	
    private final Map<String, Board> store = new HashMap<>();
    boolean called = false;
    
    @Override
    public void createBoard(CreateBoardCommand createBoardCommand) {
    	this.called = true;
    	Board board = Board.createBoard(createBoardCommand, TEST_BOARD_GUID_1);
    	store.put(board.getBoardGuid(), board);
    }

    @Override
    public Board detailBoard(String boardGuid, Boolean cookieResult) {
        return null;
    }

    @Override
    public void updateBoard(UpdateBoardCommand updateBoardCommand) {

    }

    @Override
    public void likeBoard(String userGuid, String boardGuid) {

    }

    @Override
    public void deleteBoard(String boardGuid) {

    }

    public boolean isCalled() {
    	return called;
    }

}