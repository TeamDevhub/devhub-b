package teamdevhub.devhub.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.out.ProjectMemberRepository;
import teamdevhub.devhub.core.project.port.out.ProjectRepository;
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
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public double reviewMember(ReviewUserCommand reviewUserCommand) {
        Project project = projectRepository.getProjectDetail(reviewUserCommand.projectGuid());

        validateReviewableProject(project);
        validateDuplicateReview(reviewUserCommand);
        validateProjectMembers(reviewUserCommand);
        UserReview userReview = UserReview.create(identifierProvider.generateIdentifier(), reviewUserCommand);
        userReviewRepository.save(userReview);

        return userReview.reviewScore();
    }

    private void validateReviewableProject(Project project) {
        if (!project.isProgressCompleted()) {
            throw BusinessRuleException.of(ErrorCode.PROJECT_NOT_COMPLETED);
        }
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

    private void validateProjectMembers(ReviewUserCommand reviewUserCommand) {
        validateMember(reviewUserCommand.projectGuid(), reviewUserCommand.reviewerGuid());
        validateMember(reviewUserCommand.projectGuid(), reviewUserCommand.revieweeGuid());
    }

    private void validateMember(String projectGuid, String userGuid) {
        if (!projectMemberRepository.isMember(projectGuid, userGuid)) {
            throw BusinessRuleException.of(ErrorCode.REVIEW_NOT_A_MEMBER);
        }
    }
}