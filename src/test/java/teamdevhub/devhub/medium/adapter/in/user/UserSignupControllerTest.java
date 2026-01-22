package teamdevhub.devhub.medium.adapter.in.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.adapter.in.user.controller.UserSignupController;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.user.UserSignupFacade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupControllerTest {

    private UserSignupController userSignupController;

    private UserSignupFacade userSignupFacade;

    @BeforeEach
    void init() {
        userSignupFacade = Mockito.mock(UserSignupFacade.class);
        userSignupController = new UserSignupController(userSignupFacade);
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

        when(userSignupFacade.signup(any())).thenReturn(user);

        // when
        ResponseEntity<DataApiResponseDto<SignupResponseDto>> response = userSignupController.signup(signupRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.SIGNUP_SUCCESS.getCode());
        assertThat(response.getBody().getData().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(response.getBody().getData().getUsername()).isEqualTo(TEST_USERNAME_1);

        verify(userSignupFacade).signup(any());
    }
}
