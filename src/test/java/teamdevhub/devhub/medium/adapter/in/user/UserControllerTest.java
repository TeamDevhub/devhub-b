package teamdevhub.devhub.medium.adapter.in.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.adapter.in.dto.request.user.SignupRequestDto;
import teamdevhub.devhub.adapter.in.dto.request.user.UpdateProfileRequestDto;
import teamdevhub.devhub.adapter.in.dto.response.user.SignupResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.user.UserDetailResponseDto;
import teamdevhub.devhub.adapter.in.user.UserController;
import teamdevhub.devhub.adapter.in.user.UserFacade;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserControllerTest {

    private UserController userController;
    private UserFacade userFacade;

    @BeforeEach
    void init() {
        userFacade = Mockito.mock(UserFacade.class);
        userController = new UserController(userFacade);
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

        User user = User.builder()
                .email(signupRequestDto.getEmail())
                .password(TEST_PASSWORD_1)
                .username(signupRequestDto.getUsername())
                .userRole(UserRole.USER)
                .build();

        when(userFacade.signup(any())).thenReturn(user);

        // when
        ResponseEntity<DataApiResponseDto<SignupResponseDto>> response = userController.signup(signupRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.SIGNUP_SUCCESS.getCode());
        assertThat(response.getBody().getData().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(response.getBody().getData().getUsername()).isEqualTo(TEST_USERNAME_1);
        verify(userFacade).signup(any());
    }

    @Test
    @DisplayName("유저_프로필_정보_조회에_성공하면_READ_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenFetchingUserProfile() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);
        User user = User.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .build();

        when(userFacade.getUserDetailProfile(authenticatedUser.userGuid())).thenReturn(user);

        // when
        ResponseEntity<DataApiResponseDto<UserDetailResponseDto>> response = userController.getProfile(authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(response.getBody().getData().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(response.getBody().getData().getUsername()).isEqualTo(TEST_USERNAME_1);
        verify(userFacade).getUserDetailProfile(authenticatedUser.userGuid());
    }

    @Test
    @DisplayName("유저_프로필_정보_수정에_성공하면_UPDATE_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenUpdatingUserProfile() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        doNothing().when(userFacade).updateProfile(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = userController.updateProfile(updateProfileRequestDto, authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.UPDATE_SUCCESS.getCode());
    }

    @Test
    @DisplayName("회원탈퇴에_성공하면_USER_DELETE_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenDeletingUserAccount() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);
        doNothing().when(userFacade).withdrawUser(authenticatedUser.userGuid());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = userController.withdraw(authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.USER_DELETE_SUCCESS.getCode());
        verify(userFacade).withdrawUser(authenticatedUser.userGuid());
    }
}