package teamdevhub.devhub.api.board.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.board.model.SearchBoardRequestDto;
import teamdevhub.devhub.api.web.model.request.PageRequestDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.board.port.in.Facade.BoardFacade;
import teamdevhub.devhub.core.board.port.in.Facade.model.BoardSummaryResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardFacade boardFacade;
	
	@GetMapping
	public ResponseEntity<DataListApiResponseDto<BoardSummaryResponseDto>> listBoard(
			@ModelAttribute SearchBoardRequestDto searchBoardRequestDto, PageRequestDto pageRequestDto) {
		return ResponseEntity.ok(boardFacade.listBoard(searchBoardRequestDto.toCommand(), PageCommand.of(pageRequestDto.getPage(), pageRequestDto.getSize())));
	}
}
