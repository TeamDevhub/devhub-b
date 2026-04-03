package teamdevhub.devhub.core.board.port.in.Facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.Facade.model.BoardBasicResponseDto;
import teamdevhub.devhub.core.board.port.in.Facade.model.BoardDetailResponseDto;
import teamdevhub.devhub.core.board.port.in.Facade.model.BoardSummaryResponseDto;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.core.board.port.in.command.UpdateBoardCommand;
import teamdevhub.devhub.core.board.port.in.usecase.BoardQueryUseCase;
import teamdevhub.devhub.core.board.port.in.usecase.BoardUseCase;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.shared.enums.SuccessCode;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardFacade {

    private final BoardQueryUseCase boardQueryUseCase;
    private final BoardUseCase boardUseCase;

	public DataListApiResponseDto<BoardSummaryResponseDto> listBoard(SearchBoardCommand searchBoardCommand, PageCommand pageCommand) {
		
		PageResult<Board> pagedBoardList = boardQueryUseCase.listBoard(searchBoardCommand, pageCommand);
		
		List<BoardSummaryResponseDto> boardSummaryResponseDtoList = pagedBoardList.content().stream()
				.map(board -> BoardSummaryResponseDto.builder()
						.boardBasicResponseDto(BoardBasicResponseDto.fromDomain(board))
						.likeCount(board.getLikeCount())
						.commentCount(board.getCommentCount())
		                .build())
				.toList();
		
		return DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        boardSummaryResponseDtoList,
                        PageResponseDto.from(pagedBoardList));
	}
	
	public DataApiResponseDto<Void> createBoard(CreateBoardCommand createBoardCommand) {
		boardUseCase.createBoard(createBoardCommand);
		 
		return DataApiResponseDto.successWithoutData(
                        SuccessCode.CREATE_SUCCESS);
		
	}

	public DataApiResponseDto<BoardDetailResponseDto> detailBoard(String boardGuid, Boolean cookieResult, String userGuid) {
		Board board = boardUseCase.detailBoard(boardGuid, cookieResult, userGuid);
		
		BoardSummaryResponseDto summaryBoard = BoardSummaryResponseDto.builder()
				.boardBasicResponseDto(BoardBasicResponseDto.fromDomain(board))
				.likeCount(board.getLikeCount())
				.commentCount(board.getCommentCount())
                .build();
		
		BoardDetailResponseDto responseDto = BoardDetailResponseDto.builder()
				.boardSummaryResponseDto(summaryBoard)
				.commentList(board.getCommentList())
				.userEmail(board.getUserEmail())
				.isLiked(board.isLiked())
				.build();

		return DataApiResponseDto.successWithData(
				SuccessCode.READ_SUCCESS,
				responseDto
				);
	}

	public DataApiResponseDto<Void> updateBoard(UpdateBoardCommand updateBoardCommand) {
		boardUseCase.updateBoard(updateBoardCommand);
		 
		return DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS);
	}

	public  DataApiResponseDto<Void> likeBoard(String boardGuid, String userGuid) {
		boardUseCase.likeBoard(boardGuid, userGuid);
		 
		return DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS);
	}

	public DataApiResponseDto<Void> deleteBoard(String boardGuid) {
		boardUseCase.deleteBoard(boardGuid);
		return DataApiResponseDto.successWithoutData(
                SuccessCode.DELETE_SUCCESS);
	}

}
