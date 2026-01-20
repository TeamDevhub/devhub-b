package teamdevhub.devhub.medium.adapter.in.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.adapter.in.auth.AuthFacade;
import teamdevhub.devhub.adapter.in.auth.controller.OauthController;
import teamdevhub.devhub.adapter.in.auth.dto.request.OauthLoginRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.in.user.dto.request.OauthSignupRequestDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OauthControllerTest {

    private OauthController oauthController;
    private AuthFacade authFacade;

    @BeforeEach
    void init() {
        authFacade = Mockito.mock(AuthFacade.class);
        oauthController = new OauthController(authFacade);
    }

    @Test
    @DisplayName("OAuth_회원가입에_성공하면_LoginResponseDto_를_반환한다")
    void signupWithOauth_returnsLoginResponse() {
        // given
        OauthSignupRequestDto requestDto = Mockito.mock(OauthSignupRequestDto.class);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .prefix("Bearer")
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        when(authFacade.signupWithOauth(any())).thenReturn(loginResponseDto);

        // when
        ResponseEntity<DataApiResponseDto<LoginResponseDto>> response = oauthController.signup(requestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());
        assertThat(response.getBody().getData()).isEqualTo(loginResponseDto);

        verify(authFacade).signupWithOauth(any());
    }

    @Test
    @DisplayName("OAuth_로그인에_성공하면_토큰과_헤더를_반환한다")
    void loginWithOauth_setsAuthorizationAndCookieHeaders() {
        // given
        OauthLoginRequestDto requestDto = Mockito.mock(OauthLoginRequestDto.class);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .prefix("Bearer")
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        when(authFacade.loginWithOauth(any())).thenReturn(loginResponseDto);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = oauthController.login(requestDto);

        // then
        assertThat(response.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)).isTrue();
        assertThat(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE)).isTrue();

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());
        assertThat(response.getBody().getData().getAccessToken()).isEqualTo("access-token");

        verify(authFacade).loginWithOauth(any());
    }
}
