package teamdevhub.devhub.outbound.user.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import teamdevhub.devhub.outbound.user.adapter.entity.UserReviewEntity;

public interface JpaUserReviewRepository extends JpaRepository<UserReviewEntity, String> {

    boolean existsByProjectGuidAndReviewerAndReviewee(String projectGuid, String reviewer, String reviewee);
    
    Optional<UserReviewEntity> findByProjectGuidAndReviewee(String projectGuid, String reviewee);
}
