package teamdevhub.devhub.small.adapter.in.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.UserController;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.fake.pure.usecase.FakeUserUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TestConstant.*;

class UserControllerTest {

    private UserController userController;
    private FakeUserUseCase fakeUserUseCase;

    @BeforeEach
    void init() {
        fakeUserUseCase = new FakeUserUseCase();
        userController = new UserController(fakeUserUseCase);
    }

    @Test
    void 회원가입에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .username(TEST_USERNAME)
                .introduction(TEST_INTRO)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when, then
        assertThat(userController.signup(signupRequestDto).getBody()).isNotNull();
        assertThat(userController.signup(signupRequestDto).getBody().getData().getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(userController.signup(signupRequestDto).getBody().getData().getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    void 유저_프로필_정보_조회에_성공하면_해당유저의_프로필_정보와_HTTPSTATUS_OK_를_반환한다() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);

        // when, then
        assertThat(userController.getProfile(authenticatedUser).getBody()).isNotNull();
        assertThat(userController.getProfile(authenticatedUser).getBody().getData().getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(userController.getProfile(authenticatedUser).getBody().getData().getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    void 유저_프로필_정보_수정에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);

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

    @Test
    void 회원탈퇴에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);

        // when, then
        assertThat(userController.withdraw(authenticatedUser).getBody().getCode()).isEqualTo(SuccessCode.USER_DELETE_SUCCESS.getCode());
        assertThat(fakeUserUseCase.getCurrentUserProfile(TEST_GUID).isDeleted()).isTrue();
    }
}