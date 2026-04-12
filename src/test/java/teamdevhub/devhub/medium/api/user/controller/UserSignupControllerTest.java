package teamdevhub.devhub.medium.api.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import teamdevhub.devhub.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.api.terms.model.AgreeTermsRequestDto;
import teamdevhub.devhub.api.user.controller.UserSignupController;
import teamdevhub.devhub.api.user.model.SignupRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.port.in.facade.AuthFacade;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.user.port.in.facade.UserSignupFacade;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupControllerTest {

    private UserSignupController userSignupController;

    private UserSignupFacade userSignupFacade;
    private AuthFacade authFacade;

    @BeforeEach
    void init() {
        userSignupFacade = Mockito.mock(UserSignupFacade.class);
        authFacade = Mockito.mock(AuthFacade.class);
        userSignupController = new UserSignupController(userSignupFacade, authFacade);
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
                .termsAgreementList(List.of(
                        AgreeTermsRequestDto.builder()
                                .termsGuid("TERMS1")
                                .agreed(true)
                                .build()
                ))
                .build();

        AuthResult mockResult = new AuthResult("access-token", "refresh-token");

        when(authFacade.login(any()))
                .thenReturn(mockResult);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = userSignupController.signup(signupRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());

        verify(userSignupFacade).signup(any());
    }
}
