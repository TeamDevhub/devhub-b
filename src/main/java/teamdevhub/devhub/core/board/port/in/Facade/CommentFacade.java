package teamdevhub.devhub.core.board.port.in.Facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.board.port.in.command.CreateCommentCommand;
import teamdevhub.devhub.core.board.port.in.usecase.CommentUseCase;
import teamdevhub.devhub.shared.enums.SuccessCode;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentFacade {

    private final CommentUseCase commentUseCase;

	public DataApiResponseDto<Void> createComment(CreateCommentCommand createCommentCommand) {
		commentUseCase.createComment(createCommentCommand);
		 
		return DataApiResponseDto.successWithoutData(
                        SuccessCode.CREATE_SUCCESS);
	}

}
