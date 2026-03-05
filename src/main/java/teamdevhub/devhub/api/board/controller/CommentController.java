package teamdevhub.devhub.api.board.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.board.model.CreateCommentRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.board.port.in.Facade.CommentFacade;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentFacade commentFacade;
	
	@PostMapping("/{boardGuid}/comments")
	public ResponseEntity<DataApiResponseDto<Void>> createComment(@RequestBody CreateCommentRequestDto createcommentRequestDto, @LoginUser AuthenticatedUser authenticatedUser,
			@PathVariable("boardGuid") String boardGuid) {
		return ResponseEntity.ok(commentFacade.createComment(createcommentRequestDto.toCommand(authenticatedUser.userGuid(), boardGuid)));
	}
}
