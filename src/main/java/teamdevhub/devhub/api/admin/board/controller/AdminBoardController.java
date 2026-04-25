package teamdevhub.devhub.api.admin.board.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.admin.board.model.SearchAdminBoardRequestDto;
import teamdevhub.devhub.api.web.model.request.PageRequestDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.board.port.in.Facade.BoardFacade;
import teamdevhub.devhub.core.board.port.in.Facade.model.AdminBoardResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;

@RestController
@RequestMapping("/admin/boards")
@RequiredArgsConstructor
public class AdminBoardController {
	
	private final BoardFacade boardFacade;

	@GetMapping
	public ResponseEntity<DataListApiResponseDto<AdminBoardResponseDto>> listAdminBoard(@ModelAttribute SearchAdminBoardRequestDto searchAdminBoardRequestDto, PageRequestDto pageRequestDto) {
		return ResponseEntity.ok(boardFacade.listAdminBoard(searchAdminBoardRequestDto.toCommand(), PageCommand.of((pageRequestDto.getPage()), pageRequestDto.getSize())));
	}

}
