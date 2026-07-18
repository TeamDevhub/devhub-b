package teamdevhub.devhub.fake.pure.application.port.in.usecase.board;

import teamdevhub.devhub.core.board.port.in.usecase.BoardLikeUseCase;

public class FakeBoardLikeUseCase implements BoardLikeUseCase {

	private boolean called = false;

	@Override
	public void likeBoard(String boardGuid, String userGuid) {
		this.called = true;
	}

	public boolean isCalled() {
		return called;
	}

}
