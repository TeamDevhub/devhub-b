package teamdevhub.devhub.core.user.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Getter
public class UserReview {

    private final String userReviewGuid;
    private final String projectGuid;
    private final String reviewer;
    private final String reviewee;
    private final double score;

    @Builder
    private UserReview(String userReviewGuid, String projectGuid, String reviewer, String reviewee, double score) {
        this.userReviewGuid = userReviewGuid;
        this.projectGuid = projectGuid;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.score = score;
    }

    public static UserReview create(String userReviewGuid, ReviewUserCommand reviewUserCommand) {
        validateScore(reviewUserCommand.score());
        return UserReview.builder()
                .userReviewGuid(userReviewGuid)
                .projectGuid(reviewUserCommand.projectGuid())
                .reviewer(reviewUserCommand.reviewerGuid())
                .reviewee(reviewUserCommand.revieweeGuid())
                .score(reviewUserCommand.score())
                .build();
    }

    public double reviewScore() {
        return this.score - 3.0;
    }

    private static void validateScore(double score) {
        if (score < 1.0 || score > 5.0) {
            throw DomainRuleException.of(ErrorCode.REVIEW_SCORE_INVALID);
        }
        if ((score * 2) % 1 != 0) {
            throw DomainRuleException.of(ErrorCode.REVIEW_SCORE_INVALID);
        }
    }
}
