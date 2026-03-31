package teamdevhub.devhub.core.board.application;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.domain.BoardLike;
import teamdevhub.devhub.core.board.domain.Comment;
import teamdevhub.devhub.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.core.board.port.in.command.UpdateBoardCommand;
import teamdevhub.devhub.core.board.port.in.usecase.BoardUseCase;
import teamdevhub.devhub.core.board.port.out.BoardLikeRepository;
import teamdevhub.devhub.core.board.port.out.BoardRepository;
import teamdevhub.devhub.core.board.port.out.CommentRepository;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.out.UserRepository;
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService implements BoardUseCase {
	
	private final IdentifierProvider identifierProvider;
	private final BoardRepository boardRepository;
	private final BoardLikeRepository boardLikeRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final CommentService commentService;
	
	
	@Override
	public void createBoard(CreateBoardCommand createBoardCommand) {
		String boardGuid = identifierProvider.generateIdentifier();
		Board board = Board.createBoard(createBoardCommand, boardGuid);
		boardRepository.save(board);
	}
	
	@Override
	public Board detailBoard(String boardGuid, Boolean cookieResult) {
		if(cookieResult) { boardRepository.updateViewCount(boardGuid); }
		Board boardDetail = boardRepository.detailBoard(boardGuid);
		
		Map<String, Long> boardLikes = boardLikeRepository.countByLikeCount(List.of(boardDetail.getBoardGuid()));
		Map<String, Long> boardComments = commentRepository.countByCommentCount(List.of(boardDetail.getBoardGuid()));		
        User user = userRepository.findByUserGuid(boardDetail.getUserGuid());
        
        List<Comment> commentList = commentService.commentList(boardDetail.getBoardGuid());
        
        boardDetail.fillDetailSubquery(boardLikes.getOrDefault(boardDetail.getBoardGuid(), 0L).toString(),
        		boardComments.getOrDefault(boardDetail.getBoardGuid(), 0L).toString(),
        		user.getUsername(),
        		user.getEmail(),
        		commentList
        		);
        
        return boardDetail;
	}

	@Override
	public void updateBoard(UpdateBoardCommand updateBoardCommand) {
		Board board = boardRepository.findByBoardGuid(updateBoardCommand.boardGuid());
		board.update(
				updateBoardCommand.title(),
				updateBoardCommand.categoryCd(),
				updateBoardCommand.content()
				);
		boardRepository.updateBoard(board);
	}
	
	@Override
	public void likeBoard(String boardGuid, String userGuid) {
		BoardLike boardLike = boardLikeRepository.likeBoard(boardGuid, userGuid);
		
		if(boardLike != null) {
			boardLikeRepository.deleteBoardLike(boardLike);
		} else {
			String boardLikeGuid = identifierProvider.generateIdentifier();
			boardLike = BoardLike.createBoardLike(boardGuid, userGuid, boardLikeGuid);
			boardLikeRepository.save(boardLike);
		}
	}
	
	@Override
	public void deleteBoard(String boardGuid) {
		boardRepository.deleteBoard(boardGuid);
	}
}