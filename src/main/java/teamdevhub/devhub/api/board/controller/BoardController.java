package teamdevhub.devhub.api.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.board.model.request.SearchBoardRequestDto;
import teamdevhub.devhub.api.board.model.response.BoardSummaryResponseDto;
import teamdevhub.devhub.api.web.model.request.PageRequestDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.port.in.Facade.BoardFacade;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardFacade boardFacde;
	
	@GetMapping
	public ResponseEntity<DataListApiResponseDto<BoardSummaryResponseDto>> boardList(
			@ModelAttribute SearchBoardRequestDto searchBoardRequestDto, PageRequestDto pageRequestDto) {
		PageResult<Board> pagedBoardList = boardFacde.boardList(
				searchBoardRequestDto.toCommand(), PageCommand.of(pageRequestDto.getPage(), pageRequestDto.getSize()));
		
		List<BoardSummaryResponseDto> boardSummaryResponseDtoList = pagedBoardList.content().stream()
				.map(BoardSummaryResponseDto::fromDomain)
				.toList();
		return ResponseEntity.ok(
				DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        boardSummaryResponseDtoList,
                        PageResponseDto.from(pagedBoardList))
                );
	}
}
