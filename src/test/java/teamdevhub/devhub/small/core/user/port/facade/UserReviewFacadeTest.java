package teamdevhub.devhub.small.core.user.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.core.user.port.in.facade.UserReviewFacade;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.project.FakeProjectMemberUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserProfileUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserReviewUseCase;
import teamdevhub.devhub.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserReviewFacadeTest {

    private UserReviewFacade userReviewFacade;

    private FakeProjectMemberUseCase projectMemberUseCase;
    private FakeUserReviewUseCase userReviewUseCase;
    private FakeUserProfileUseCase userProfileUseCase;

    @BeforeEach
    void init() {
        projectMemberUseCase = new FakeProjectMemberUseCase();
        userReviewUseCase = new FakeUserReviewUseCase();
        userProfileUseCase = new FakeUserProfileUseCase();

        userReviewFacade = new UserReviewFacade(
                projectMemberUseCase,
                userReviewUseCase,
                userProfileUseCase
        );
    }

    private ReviewUserCommand command(double score) {
        return ReviewUserCommand.builder()
                .userReviewGuid(TEST_REVIEW_GUID_1)
                .projectGuid(TEST_PROJECT_GUID_1)
                .reviewerGuid(TEST_USER_GUID_1)
                .revieweeGuid(TEST_USER_GUID_2)
                .score(score)
                .build();
    }

    @Test
    @DisplayName("리뷰_요청을_하면_검증_후_리뷰가_저장되고_매너도가_업데이트된다")
    void reviewMember_validCommand_delegatesCorrectly() {
        // given
        userReviewUseCase.willReturn(4.0);

        // when
        userReviewFacade.reviewMember(command(4.0));

        // then
        assertThat(projectMemberUseCase.called).isTrue();
        assertThat(userReviewUseCase.called).isTrue();
        assertThat(userProfileUseCase.called).isTrue();

        assertThat(userProfileUseCase.lastUserGuid).isEqualTo(TEST_USER_GUID_2);
        assertThat(userProfileUseCase.lastScore).isEqualTo(4.0);
    }

    @Test
    @DisplayName("검증에서_예외가_발생하면_리뷰와_매너도_업데이트는_실행되지_않는다")
    void reviewMember_validationFails_stopsFlow() {
        // given
        projectMemberUseCase.willThrow(
                BusinessRuleException.of(ErrorCode.PROJECT_NOT_COMPLETED)
        );

        // when & then
        assertThatThrownBy(() ->
                userReviewFacade.reviewMember(command(4.0))
        ).isInstanceOf(BusinessRuleException.class);

        assertThat(userReviewUseCase.called).isFalse();
        assertThat(userProfileUseCase.called).isFalse();
    }
}