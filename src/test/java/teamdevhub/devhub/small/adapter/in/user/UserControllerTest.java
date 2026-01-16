package teamdevhub.devhub.small.adapter.in.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.UserController;
import teamdevhub.devhub.adapter.in.dto.request.user.SignupRequestDto;
import teamdevhub.devhub.adapter.in.dto.request.user.UpdateProfileRequestDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.AuthenticatedUser;
import teamdevhub.devhub.fake.pure.usecase.FakeUserUseCase;
import teamdevhub.devhub.fake.pure.usecase.FakeUserWithdrawUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserControllerTest {

    private UserController userController;
    private FakeUserUseCase fakeUserUseCase;

    @BeforeEach
    void init() {
        fakeUserUseCase = new FakeUserUseCase();
        FakeUserWithdrawUseCase fakeUserWithdrawUseCase = new FakeUserWithdrawUseCase();
        userController = new UserController(fakeUserUseCase, fakeUserWithdrawUseCase);
    }

    @Test
    @DisplayName("회원가입에_성공하면_SIGNUP_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenSignupSucceed() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when, then
        assertThat(userController.signup(signupRequestDto).getBody()).isNotNull();
        assertThat(userController.signup(signupRequestDto).getBody().getCode()).isEqualTo(SuccessCode.SIGNUP_SUCCESS.getCode());
        assertThat(userController.signup(signupRequestDto).getBody().getData().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(userController.signup(signupRequestDto).getBody().getData().getUsername()).isEqualTo(TEST_USERNAME_1);
    }

    @Test
    @DisplayName("유저_프로필_정보_조회에_성공하면_해당유저의_프로필_정보와_READ_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenFetchingUserProfile() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);

        // when, then
        assertThat(userController.getProfile(authenticatedUser).getBody()).isNotNull();
        assertThat(userController.getProfile(authenticatedUser).getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(userController.getProfile(authenticatedUser).getBody().getData().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(userController.getProfile(authenticatedUser).getBody().getData().getUsername()).isEqualTo(TEST_USERNAME_1);
    }

    @Test
    @DisplayName("유저_프로필_정보_수정에_성공하면_UPDATE_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenUpdatingUserProfile() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);

        // when
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        // then
        assertThat(userController.updateProfile(updateProfileRequestDto, authenticatedUser).getBody().getCode()).isEqualTo(SuccessCode.UPDATE_SUCCESS.getCode());
    }

//    @Test
//    @DisplayName("회원탈퇴에_성공하면_USER_DELETE_SUCCESS_의_코드를_확인할_수_있다")
//    void canVerifyCodeWhenDeletingUserAccount() {
//        // given
//        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);
//
//        // when, then
//        assertThat(userController.withdraw(authenticatedUser).getBody().getCode()).isEqualTo(SuccessCode.USER_DELETE_SUCCESS.getCode());
//        assertThat(fakeUserUseCase.getCurrentUserProfile(TEST_USER_GUID_1).isDeleted()).isTrue();
//    }
}