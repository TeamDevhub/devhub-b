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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.board.model.CreateBoardRequestDto;
import teamdevhub.devhub.api.board.model.DeleteBoardRequestDto;
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

	@GetMapping
	public ResponseEntity<DataListApiResponseDto<BoardSummaryResponseDto>> listBoard(@ModelAttribute SearchBoardRequestDto searchBoardRequestDto, PageRequestDto pageRequestDto) {
		return ResponseEntity.ok(boardFacade.listBoard(searchBoardRequestDto.toCommand(), PageCommand.of((pageRequestDto.getPage()), pageRequestDto.getSize())));
	}
	
	@PostMapping
	public ResponseEntity<DataApiResponseDto<Void>> createBoard(@RequestBody CreateBoardRequestDto createBoardRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
		return ResponseEntity.ok(boardFacade.createBoard(createBoardRequestDto.toCommand(authenticatedUser.userGuid())));
	}

	@GetMapping("/{boardGuid}")
	public ResponseEntity<DataApiResponseDto<BoardDetailResponseDto>> detailBoard(@PathVariable("boardGuid") String boardGuid, 
			HttpServletRequest request, HttpServletResponse response, @LoginUser AuthenticatedUser authenticatedUser) {
		boolean cookieResult = isCookie(boardGuid, request, response);
		return ResponseEntity.ok(boardFacade.detailBoard(boardGuid, cookieResult, authenticatedUser.userGuid()));
	}
	
	private boolean isCookie(String boardGuid, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("boardView")) {
                	if(cookie.getValue().contains("[" + boardGuid + "]")) return false;
                	
                	cookie.setValue(cookie.getValue() + "_[" + boardGuid + "]");
                	cookie.setPath("/");
                	cookie.setMaxAge(60 * 60 * 24);
                	response.addCookie(cookie);
                	return true;
                }
            }
        }
        
        Cookie newCookie = new Cookie("boardView","[" + boardGuid + "]");
        newCookie.setPath("/");
        newCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(newCookie);
        return true;
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
	
	@PostMapping("/delete")
	public ResponseEntity<DataApiResponseDto<Void>> deleteBoard(@RequestBody DeleteBoardRequestDto deleteBoardRequestDto) {
		return ResponseEntity.ok(boardFacade.deleteBoard(deleteBoardRequestDto.getBoardGuids()));
	}
}
