package teamdevhub.devhub.core.user.port.out;

import teamdevhub.devhub.core.user.domain.UserReview;

public interface UserReviewRepository {

    void save(UserReview userReview);

    boolean existsByProjectGuidAndReviewerAndReviewee(String projectGuid, String reviewer, String reviewee);
}
