package teamdevhub.devhub.small.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.application.service.UserReviewService;
import teamdevhub.devhub.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserReviewRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserReviewServiceTest {

    private UserReviewService userReviewService;
    private FakeUserReviewRepository userReviewRepository;

    @BeforeEach
    void init() {
        userReviewRepository = new FakeUserReviewRepository();

        userReviewService = new UserReviewService(
                new FakeUuidIdentifierProvider(TEST_REVIEW_GUID_1),
                userReviewRepository
        );
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
        ReviewUserCommand reviewUserCommand =
                reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_2, 4.0);

        // when
        double reviewScore = userReviewService.reviewMember(reviewUserCommand);

        // then
        assertThat(userReviewRepository.findAll()).hasSize(1);
        assertThat(reviewScore).isEqualTo(4.0);
    }

    @Test
    @DisplayName("동일_프로젝트에서_같은_대상을_중복_리뷰하면_예외가_발생한다")
    void reviewMember_duplicateReview_throwsException() {
        // given
        ReviewUserCommand reviewUserCommand =
                reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_2, 3.0);

        userReviewService.reviewMember(reviewUserCommand);

        // when
        ReviewUserCommand duplicateCommand =
                reviewCommand(TEST_USER_GUID_1, TEST_USER_GUID_2, 5.0);

        // then
        assertThatThrownBy(() -> userReviewService.reviewMember(duplicateCommand))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REVIEW_DUPLICATE.getMessage());
    }
}