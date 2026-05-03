package teamdevhub.devhub.outbound.user.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.user.domain.UserReview;
import teamdevhub.devhub.core.user.port.out.UserReviewRepository;
import teamdevhub.devhub.outbound.user.adapter.entity.UserReviewEntity;
import teamdevhub.devhub.outbound.user.persistence.JpaUserReviewRepository;

@Component
@RequiredArgsConstructor
public class UserReviewAdapter implements UserReviewRepository {

    private final JpaUserReviewRepository jpaUserReviewRepository;

    @Override
    public void save(UserReview userReview) {
        jpaUserReviewRepository.save(toEntity(userReview));
    }

    @Override
    public boolean existsByProjectGuidAndReviewerAndReviewee(String projectGuid, String reviewer, String reviewee) {
        return jpaUserReviewRepository.existsByProjectGuidAndReviewerAndReviewee(projectGuid, reviewer, reviewee);
    }

    private UserReviewEntity toEntity(UserReview userReview) {
        return UserReviewEntity.builder()
                .userReviewGuid(userReview.getUserReviewGuid())
                .projectGuid(userReview.getProjectGuid())
                .reviewer(userReview.getReviewer())
                .reviewee(userReview.getReviewee())
                .score(userReview.getScore())
                .build();
    }
}
