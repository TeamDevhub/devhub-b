package teamdevhub.devhub.small.adapter.in.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.adapter.in.user.UserController;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.adapter.in.user.dto.response.UserProfileResponseDto;
import teamdevhub.devhub.domain.common.record.auth.LoginUser;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.small.mock.usecase.FakeUserUseCase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    private static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USERNAME = "User";
    private static final String TEST_INTRO = "Hello World";
    private static final List<String> TEST_POSITION_LIST = List.of("001");
    private static final List<String> TEST_SKILL_LIST = List.of("001");

    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";
    private static final List<String> NEW_POSITION_LIST = List.of("002");
    private static final List<String> NEW_SKILL_LIST = List.of("002");

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
        ResponseEntity<ApiDataResponseVo<SignupResponseDto>> response = userController.signup(signupRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(response.getBody().getData().getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    void 유저_프로필_정보_조회에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        //given
        LoginUser loginUser = new LoginUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);

        //when
        ResponseEntity<ApiDataResponseVo<UserProfileResponseDto>> response = userController.getProfile(loginUser);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(response.getBody().getData().getUsername()).isEqualTo(TEST_USERNAME);
    }

    @Test
    void 유저_프로필_정보_수정에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        //given
        LoginUser loginUser = new LoginUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);

        //when
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        ResponseEntity<ApiDataResponseVo<Void>> response = userController.updateProfile(updateProfileRequestDto, loginUser);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 회원탈퇴에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        //given
        LoginUser loginUser = new LoginUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);

        //when
        ResponseEntity<ApiDataResponseVo<Void>> response = userController.withdraw(loginUser);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fakeUserUseCase.getCurrentUserProfile(TEST_GUID).isDeleted()).isTrue();
    }
}