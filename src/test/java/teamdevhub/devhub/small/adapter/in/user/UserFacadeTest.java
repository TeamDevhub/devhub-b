package teamdevhub.devhub.small.adapter.in.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.UserFacade;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.fake.pure.usecase.user.FakeUserProfileUseCase;
import teamdevhub.devhub.fake.pure.usecase.user.FakeUserSignupUseCase;
import teamdevhub.devhub.fake.pure.usecase.user.FakeUserWithdrawUseCase;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserFacadeTest {

    private UserFacade userFacade;
    private FakeUserProfileUseCase fakeUserProfileUseCase;
    private FakeUserWithdrawUseCase fakeUserWithdrawUseCase;

    @BeforeEach
    void init() {
        FakeUserSignupUseCase fakeUserSignupUseCase = new FakeUserSignupUseCase();
        fakeUserProfileUseCase = new FakeUserProfileUseCase();
        fakeUserWithdrawUseCase = new FakeUserWithdrawUseCase();

        userFacade = new UserFacade(
                fakeUserSignupUseCase,
                fakeUserProfileUseCase,
                fakeUserWithdrawUseCase
        );
    }

    @Test
    @DisplayName("signup_는_사용자_정보를_정상적으로_반환한다")
    void signupReturnsUser() {
        // given
        SignupCommand signupCommand = new SignupCommand(
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST,
                VERIFICATION_TARGET_1
        );

        // when
        User user = userFacade.signup(signupCommand);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO_1);
    }

    @Test
    @DisplayName("getUserDetailProfile_는_해당_유저_정보를_반환한다")
    void getUserDetailProfileReturnsUser() {
        // given, when
        User user = userFacade.getUserDetailProfile(TEST_USER_GUID_1);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("updateProfile_는_유저_프로필을_업데이트한다")
    void updateProfileUpdatesUser() {
        // given
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .build();

        // when
        userFacade.updateProfile(updateProfileCommand);

        // then
        User updatedUser = fakeUserProfileUseCase.getCurrentUserProfile(TEST_USER_GUID_1);
        assertThat(updatedUser.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(updatedUser.getIntroduction()).isEqualTo(NEW_INTRO);
    }

    @Test
    @DisplayName("withdrawUser_는_유저를_삭제 처리한다")
    void withdrawUserDeletesUser() {
        // given, when
        userFacade.withdrawUser(TEST_USER_GUID_1);

        // then
        User deletedUser = fakeUserWithdrawUseCase.getUser(TEST_USER_GUID_1);
        assertThat(deletedUser.isDeleted()).isTrue();
    }
}
