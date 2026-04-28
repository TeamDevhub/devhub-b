package teamdevhub.devhub.core.user.port.in.usecase;

import teamdevhub.devhub.core.user.port.in.command.ReviewUserCommand;

public interface UserReviewUseCase {

    double reviewMember(ReviewUserCommand command);
}
