package teamdevhub.devhub.medium.api.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import teamdevhub.devhub.api.auth.controller.AuthController;
import teamdevhub.devhub.api.auth.model.request.LoginRequestDto;
import teamdevhub.devhub.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.api.auth.controller.CookieFactory;
import teamdevhub.devhub.core.auth.port.in.facade.AuthFacade;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthControllerTest {

    private AuthController authController;

    private AuthFacade authFacade;

    @BeforeEach
    void init() {
        authFacade = Mockito.mock(AuthFacade.class);

        authController = new AuthController(authFacade, new CookieFactory());
    }

    @Test
    @DisplayName("로그인에_성공하면_LOGIN_SUCCESS_코드를_확인할_수_있다")
    void canVerifyCodeWhenLoginSucceed() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        AuthResult authResult = AuthResult.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        when(authFacade.login(any())).thenReturn(authResult);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = authController.login(loginRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());

        HttpHeaders headers = response.getHeaders();
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
        assertThat(cookies).isNotNull();

        verify(authFacade).login(any());
    }

    @Test
    @DisplayName("토큰_재발급에_성공하면_CREATE_SUCCESS_코드와_새_리프레시_쿠키를_확인할_수_있다")
    void canVerifyCodeWhenRefreshingToken() {
        // given
        AuthResult authResult = AuthResult.of("new-access-token", "new-refresh-token");
        String oldRefreshToken = "old-refresh-token";
        when(authFacade.reissueAccessToken(oldRefreshToken)).thenReturn(authResult);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = authController.refresh(oldRefreshToken);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.CREATE_SUCCESS.getCode());

        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(cookies).isNotNull();
        assertThat(cookies).anyMatch(c -> c.contains("new-refresh-token"));

        verify(authFacade).reissueAccessToken(oldRefreshToken);
    }

    @Test
    @DisplayName("로그아웃에_성공하면_LOGOUT_SUCCESS_코드를_확인할_수_있다")
    void canVerifyCodeWhenLogoutSucceed() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );

        doNothing().when(authFacade).logout(TEST_USER_GUID_1);

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = authController.logout(authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGOUT_SUCCESS.getCode());

        verify(authFacade).logout(TEST_USER_GUID_1);
    }
}