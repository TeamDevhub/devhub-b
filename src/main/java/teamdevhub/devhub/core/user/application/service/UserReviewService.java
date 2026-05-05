package teamdevhub.devhub.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.user.domain.UserReview;
import teamdevhub.devhub.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.UserReviewUseCase;
import teamdevhub.devhub.core.user.port.out.UserReviewRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReviewService implements UserReviewUseCase {

    private final IdentifierProvider identifierProvider;
    private final UserReviewRepository userReviewRepository;

    @Override
    public double reviewMember(ReviewUserCommand reviewUserCommand) {

        validateDuplicateReview(reviewUserCommand);
        UserReview userReview = UserReview.create(identifierProvider.generateIdentifier(), reviewUserCommand);
        userReviewRepository.save(userReview);
        return userReview.getScore();
    }

    private void validateDuplicateReview(ReviewUserCommand reviewUserCommand) {
        boolean alreadyReviewed = userReviewRepository.existsByProjectGuidAndReviewerAndReviewee(
                reviewUserCommand.projectGuid(),
                reviewUserCommand.reviewerGuid(),
                reviewUserCommand.revieweeGuid()
        );

        if (alreadyReviewed) {
            throw BusinessRuleException.of(ErrorCode.REVIEW_DUPLICATE);
        }
    }
}