package teamdevhub.devhub.api.board.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.board.model.CreateBoardRequestDto;
import teamdevhub.devhub.api.board.model.SearchBoardRequestDto;
import teamdevhub.devhub.api.board.model.UpdateBoardRequestDto;
import teamdevhub.devhub.api.web.model.request.PageRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.board.port.in.Facade.BoardFacade;
import teamdevhub.devhub.core.board.port.in.Facade.model.BoardDetailResponseDto;
import teamdevhub.devhub.core.board.port.in.Facade.model.BoardSummaryResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardFacade boardFacade;

	/**
	 * 리뷰
	 * PageCommand.of((pageRequestDto.getPage()-1) 이 부분이 정확히 어떤 의미일까요? 페이지가 0부터 시작하기 때문에 해당 소스처럼 작성하신걸까요?
	 * @param searchBoardRequestDto
	 * @param pageRequestDto
	 * @return
	 */
	@GetMapping
	public ResponseEntity<DataListApiResponseDto<BoardSummaryResponseDto>> listBoard(@ModelAttribute SearchBoardRequestDto searchBoardRequestDto, PageRequestDto pageRequestDto) {
		return ResponseEntity.ok(boardFacade.listBoard(searchBoardRequestDto.toCommand(), PageCommand.of((pageRequestDto.getPage()-1), pageRequestDto.getSize())));
	}
	
	@PostMapping
	public ResponseEntity<DataApiResponseDto<Void>> createBoard(@RequestBody CreateBoardRequestDto createBoardRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
		return ResponseEntity.ok(boardFacade.createBoard(createBoardRequestDto.toCommand(authenticatedUser.userGuid())));
	}

	/**
	 * 퍼사드로 진입하는 요청은 외부 통신 규경인 HttpServletRequest/Response 와는 무관하게 진행되어야하는 것으로 판단됩니다.
	 * 조회 수 관련 로직해서 해당 request, response 객체가 필수적으로 필요한 사항일까요?
	 * @param boardGuid
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/{boardGuid}")
	public ResponseEntity<DataApiResponseDto<BoardDetailResponseDto>> detailBoard(@PathVariable("boardGuid") String boardGuid, 
			HttpServletRequest request, HttpServletResponse response) {
		return ResponseEntity.ok(boardFacade.detailBoard(boardGuid, request, response));
	}
	
	@PutMapping("/{boardGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> updateBoard(@RequestBody UpdateBoardRequestDto updateBoardRequestDto, @LoginUser AuthenticatedUser authenticatedUser, 
			@PathVariable("boardGuid") String boardGuid) {
		return ResponseEntity.ok(boardFacade.updateBoard(updateBoardRequestDto.toCommand(authenticatedUser.userGuid(), boardGuid)));
	}
		
	@PostMapping("/{boardGuid}/likes")
	public ResponseEntity<DataApiResponseDto<Void>> likeBoard(@PathVariable("boardGuid") String boardGuid, @LoginUser AuthenticatedUser authenticatedUser) {
		return ResponseEntity.ok(boardFacade.likeBoard(boardGuid, authenticatedUser.userGuid()));
	}
}
