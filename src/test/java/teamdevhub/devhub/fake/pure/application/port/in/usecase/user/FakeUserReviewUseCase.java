package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.UserReviewUseCase;

public class FakeUserReviewUseCase implements UserReviewUseCase {

    private double lastReviewScore;
    private String lastRevieweeGuid;

    @Override
    public double reviewMember(ReviewUserCommand command) {
        lastReviewScore = command.score() - 3.0;
        lastRevieweeGuid = command.revieweeGuid();
        return lastReviewScore;
    }

    public double getLastReviewScore() {
        return lastReviewScore;
    }

    public String getLastRevieweeGuid() {
        return lastRevieweeGuid;
    }
}
