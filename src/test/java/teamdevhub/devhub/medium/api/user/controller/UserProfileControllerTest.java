package teamdevhub.devhub.medium.api.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import teamdevhub.devhub.api.user.controller.UserProfileController;
import teamdevhub.devhub.api.user.model.request.UpdateProfileRequestDto;
import teamdevhub.devhub.api.user.model.response.UserDetailResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.port.in.facade.UserWithdrawFacade;
import teamdevhub.devhub.core.user.port.in.usecase.UserProfileUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserProfileControllerTest {

    private UserProfileController userProfileController;

    private UserProfileUseCase userProfileUseCase;
    private UserWithdrawFacade userWithdrawFacade;

    @BeforeEach
    void init() {
        userProfileUseCase = Mockito.mock(UserProfileUseCase.class);
        userWithdrawFacade = Mockito.mock(UserWithdrawFacade.class);

        userProfileController = new UserProfileController(userProfileUseCase, userWithdrawFacade);
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

        when(userProfileUseCase.getCurrentUserProfile(authenticatedUser.userGuid())).thenReturn(user);

        // when
        ResponseEntity<DataApiResponseDto<UserDetailResponseDto>> response = userProfileController.getProfile(authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(response.getBody().getData().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(response.getBody().getData().getUsername()).isEqualTo(TEST_USERNAME_1);

        verify(userProfileUseCase).getCurrentUserProfile(authenticatedUser.userGuid());
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

        doNothing().when(userProfileUseCase).updateProfile(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = userProfileController.updateProfile(updateProfileRequestDto, authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.UPDATE_SUCCESS.getCode());
    }

    @Test
    @DisplayName("회원탈퇴에_성공하면_USER_DELETE_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenDeletingUserAccount() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);
        doNothing().when(userWithdrawFacade).withdraw(authenticatedUser.userGuid());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = userProfileController.withdraw(authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.USER_DELETE_SUCCESS.getCode());

        verify(userWithdrawFacade).withdraw(authenticatedUser.userGuid());
    }
}