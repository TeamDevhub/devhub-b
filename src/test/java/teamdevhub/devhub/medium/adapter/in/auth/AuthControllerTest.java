package teamdevhub.devhub.medium.adapter.in.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.adapter.in.auth.controller.AuthController;
import teamdevhub.devhub.adapter.in.auth.AuthFacade;
import teamdevhub.devhub.adapter.in.auth.dto.request.LoginRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthControllerTest {

    private AuthController authController;
    private AuthFacade authFacade;

    @BeforeEach
    void init() {
        authFacade = Mockito.mock(AuthFacade.class);
        authController = new AuthController(authFacade);
    }

    @Test
    @DisplayName("로그인에_성공하면_LOGIN_SUCCESS_코드를_확인할_수_있다")
    void canVerifyCodeWhenLoginSucceed() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        when(authFacade.login(any())).thenReturn(loginResponseDto);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = authController.login(loginRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());
        assertThat(response.getBody().getData()).isNotNull();

        verify(authFacade).login(any());
    }

    @Test
    @DisplayName("토큰_재발급에_성공하면_CREATE_SUCCESS_코드를_확인할_수_있다")
    void canVerifyCodeWhenRefreshingToken() {
        // given
        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken("new-access-token")
                .build();
        String refreshToken = "refresh-token";
        when(authFacade.reissueAccessToken(refreshToken)).thenReturn(tokenResponseDto);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = authController.refresh(refreshToken);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.CREATE_SUCCESS.getCode());

        verify(authFacade).reissueAccessToken(refreshToken);
    }

    @Test
    @DisplayName("로그아웃에_성공하면_LOGOUT_SUCCESS_코드를_확인할_수_있다")
    void canVerifyCodeWhenLogoutSucceed() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        doNothing().when(authFacade).logout(TEST_USER_GUID_1);

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = authController.revoke(authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGOUT_SUCCESS.getCode());

        verify(authFacade).logout(TEST_USER_GUID_1);
    }
}