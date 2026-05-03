package teamdevhub.devhub.small.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.user.application.service.UserReviewService;
import teamdevhub.devhub.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.project.FakeProjectMemberRepository;
import teamdevhub.devhub.fake.pure.application.port.out.project.FakeProjectRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserReviewRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserReviewServiceTest {

    private UserReviewService userReviewService;
    private FakeUserReviewRepository userReviewRepository;
    private FakeProjectRepository projectRepository;
    private FakeProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void init() {
        userReviewRepository = new FakeUserReviewRepository();
        projectRepository = new FakeProjectRepository();
        projectMemberRepository = new FakeProjectMemberRepository();

        userReviewService = new UserReviewService(
                new FakeUuidIdentifierProvider(TEST_REVIEW_GUID_1),
                userReviewRepository,
                projectRepository,
                projectMemberRepository
        );
    }

    private Project completedProject() {
        return Project.builder()
                .projectGuid(TEST_PROJECT_GUID_1)
                .userGuid(TEST_USER_GUID_1)
                .progressEndDate(LocalDate.now().minusDays(1))
                .build();
    }

    private Project incompleteProject() {
        return Project.builder()
                .projectGuid(TEST_PROJECT_GUID_1)
                .userGuid(TEST_USER_GUID_1)
                .progressEndDate(LocalDate.now().plusDays(30))
                .build();
    }

    private ReviewUserCommand reviewCommand(String reviewerGuid, String revieweeGuid, double score) {
        return ReviewUserCommand.builder()
                .userReviewGuid(TEST_REVIEW_GUID_1)
                .projectGuid(TEST_PROJECT_GUID_1)
                .reviewerGuid(reviewerGuid)
                .revieweeGuid(revieweeGuid)
                .score(score)
                .build();
    }

    @Test
    @DisplayName("유효한_요청으로_리뷰하면_리뷰가_저장되고_raw_점수가_반환된다")
    void reviewMember_validRequest_savesReviewAndReturnsRawScore() {
        // given
        projectRepository.save(completedProject());
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_1);
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_2);

        ReviewUserCommand reviewUserCommand = reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_2, 4.0);

        // when
        double reviewScore = userReviewService.reviewMember(reviewUserCommand);

        // then
        assertThat(userReviewRepository.findAll()).hasSize(1);
        assertThat(reviewScore).isEqualTo(4.0);
    }

    @Test
    @DisplayName("프로젝트가_완료되지_않으면_예외가_발생한다")
    void reviewMember_projectNotCompleted_throwsException() {
        // given
        projectRepository.save(incompleteProject());
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_1);
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_2);

        ReviewUserCommand reviewUserCommand = reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_2, 4.0);

        // when, then
        assertThatThrownBy(() -> userReviewService.reviewMember(reviewUserCommand))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.PROJECT_NOT_COMPLETED.getMessage());
    }

    @Test
    @DisplayName("자기_자신을_리뷰하면_예외가_발생한다")
    void reviewMember_selfReview_throwsException() {
        // given
        projectRepository.save(completedProject());
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_1);

        ReviewUserCommand reviewUserCommand = reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_1, 3.0);

        // when, then
        assertThatThrownBy(() -> userReviewService.reviewMember(reviewUserCommand))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.REVIEW_SELF_NOT_ALLOWED.getMessage());
    }

    @Test
    @DisplayName("동일_프로젝트에서_같은_대상을_중복_리뷰하면_예외가_발생한다")
    void reviewMember_duplicateReview_throwsException() {
        // given
        projectRepository.save(completedProject());
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_1);
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_2);

        ReviewUserCommand reviewUserCommand = reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_2, 3.0);
        userReviewService.reviewMember(reviewUserCommand);

        // when, then
        ReviewUserCommand duplicateCommand = reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_2, 5.0);
        assertThatThrownBy(() -> userReviewService.reviewMember(duplicateCommand))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REVIEW_DUPLICATE.getMessage());
    }

    @Test
    @DisplayName("리뷰어가_프로젝트_멤버가_아니면_예외가_발생한다")
    void reviewMember_reviewerNotMember_throwsException() {
        // given
        projectRepository.save(completedProject());
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_2);

        ReviewUserCommand reviewUserCommand = reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_2, 3.0);

        // when, then
        assertThatThrownBy(() -> userReviewService.reviewMember(reviewUserCommand))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REVIEW_NOT_A_MEMBER.getMessage());
    }

    @Test
    @DisplayName("리뷰이가_프로젝트_멤버가_아니면_예외가_발생한다")
    void reviewMember_revieweeNotMember_throwsException() {
        // given
        projectRepository.save(completedProject());
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_1);

        ReviewUserCommand reviewUserCommand = reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_2, 3.0);

        // when, then
        assertThatThrownBy(() -> userReviewService.reviewMember(reviewUserCommand))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REVIEW_NOT_A_MEMBER.getMessage());
    }
}
