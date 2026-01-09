package teamdevhub.devhub.small.adapter.in.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataResponseDto;
import teamdevhub.devhub.adapter.in.user.UserController;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.adapter.in.user.dto.response.UserProfileResponseDto;
import teamdevhub.devhub.domain.common.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.small.mock.usecase.FakeUserUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.constant.TestConstant.*;

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
        //given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .username(TEST_USERNAME)
                .introduction(TEST_INTRO)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        //when
        ResponseEntity<ApiDataResponseDto<SignupResponseDto>> response = userController.signup(signupRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(response.getBody().getData().getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    void 유저_프로필_정보_조회에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        //given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);

        //when
        ResponseEntity<ApiDataResponseDto<UserProfileResponseDto>> response = userController.getProfile(authenticatedUser);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(response.getBody().getData().getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    void 유저_프로필_정보_수정에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        //given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);

        //when
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        ResponseEntity<ApiDataResponseDto<Void>> response = userController.updateProfile(updateProfileRequestDto, authenticatedUser);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 회원탈퇴에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        //given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);

        //when
        ResponseEntity<ApiDataResponseDto<Void>> response = userController.withdraw(authenticatedUser);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fakeUserUseCase.getCurrentUserProfile(TEST_GUID).isDeleted()).isTrue();
    }
}