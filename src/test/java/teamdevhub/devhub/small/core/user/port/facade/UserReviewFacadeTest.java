package teamdevhub.devhub.small.core.user.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.core.user.port.in.facade.UserReviewFacade;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserProfileUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserReviewUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserReviewFacadeTest {

    private UserReviewFacade userReviewFacade;
    private FakeUserReviewUseCase userReviewUseCase;
    private FakeUserProfileUseCase userProfileUseCase;

    @BeforeEach
    void init() {
        userReviewUseCase = new FakeUserReviewUseCase();
        userProfileUseCase = new FakeUserProfileUseCase();

        userReviewFacade = new UserReviewFacade(userReviewUseCase, userProfileUseCase);
    }

    @Test
    @DisplayName("리뷰_요청을_하면_리뷰가_저장되고_매너도가_업데이트된다")
    void reviewMember_validCommand_delegatesReviewAndUpdatesManner() {
        // given
        ReviewUserCommand command = ReviewUserCommand.builder()
                .userReviewGuid(TEST_REVIEW_GUID_1)
                .projectGuid(TEST_PROJECT_GUID_1)
                .reviewerGuid(TEST_USER_GUID_1)
                .revieweeGuid(TEST_USER_GUID_2)
                .score(4.0)
                .build();

        // when
        userReviewFacade.reviewMember(command);

        // then
        assertThat(userReviewUseCase.getLastRevieweeGuid()).isEqualTo(TEST_USER_GUID_2);
        assertThat(userReviewUseCase.getLastReviewScore()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("점수_3.0으로_리뷰하면_매너도_변화량은_0이다")
    void reviewMember_score3_mannerDeltaIsZero() {
        // given
        ReviewUserCommand command = ReviewUserCommand.builder()
                .userReviewGuid(TEST_REVIEW_GUID_1)
                .projectGuid(TEST_PROJECT_GUID_1)
                .reviewerGuid(TEST_USER_GUID_1)
                .revieweeGuid(TEST_USER_GUID_2)
                .score(3.0)
                .build();

        // when
        userReviewFacade.reviewMember(command);

        // then
        assertThat(userReviewUseCase.getLastReviewScore()).isEqualTo(0.0);
    }
}
