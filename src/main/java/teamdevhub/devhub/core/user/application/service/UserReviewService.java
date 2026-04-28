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
import teamdevhub.devhub.core.user.port.out.UserRepository;
import teamdevhub.devhub.core.user.port.out.UserReviewRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReviewService implements UserReviewUseCase {

    private final IdentifierProvider identifierProvider;
    private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public double reviewMember(ReviewUserCommand reviewUserCommand) {
        Project project = projectRepository.getProjectDetail(reviewUserCommand.projectGuid());

        if (!project.isProgressCompleted()) {
            throw BusinessRuleException.of(ErrorCode.PROJECT_NOT_COMPLETED);
        }

        if (reviewUserCommand.reviewerGuid().equals(reviewUserCommand.revieweeGuid())) {
            throw BusinessRuleException.of(ErrorCode.REVIEW_SELF_NOT_ALLOWED);
        }

        if (userReviewRepository.existsByProjectGuidAndReviewerAndReviewee(
                reviewUserCommand.projectGuid(), reviewUserCommand.reviewerGuid(), reviewUserCommand.revieweeGuid())) {
            throw BusinessRuleException.of(ErrorCode.REVIEW_DUPLICATE);
        }

        if (!projectMemberRepository.isMember(reviewUserCommand.projectGuid(), reviewUserCommand.reviewerGuid())) {
            throw BusinessRuleException.of(ErrorCode.REVIEW_NOT_A_MEMBER);
        }

        if (!projectMemberRepository.isMember(reviewUserCommand.projectGuid(), reviewUserCommand.revieweeGuid())) {
            throw BusinessRuleException.of(ErrorCode.REVIEW_NOT_A_MEMBER);
        }

        UserReview userReview = UserReview.create(identifierProvider.generateIdentifier(), reviewUserCommand);
        userReviewRepository.save(userReview);
        return userReview.reviewScore();
    }
}
