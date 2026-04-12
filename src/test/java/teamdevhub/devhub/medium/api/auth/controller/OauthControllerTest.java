package teamdevhub.devhub.medium.api.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.api.auth.controller.OauthController;
import teamdevhub.devhub.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.api.user.model.SignupOauthRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthResult;
import teamdevhub.devhub.core.auth.port.in.facade.OauthAuthFacade;
import teamdevhub.devhub.core.user.port.in.facade.UserSignupFacade;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OauthControllerTest {

    private OauthController oauthController;

    private UserSignupFacade userSignupFacade;
    private OauthAuthFacade oauthAuthFacade;

    @BeforeEach
    void init() {
        userSignupFacade = Mockito.mock(UserSignupFacade.class);
        oauthAuthFacade = Mockito.mock(OauthAuthFacade.class);

        oauthController = new OauthController(userSignupFacade, oauthAuthFacade);
    }


    @Test
    @DisplayName("OAuth_로그인_요청시_Provider_인증_URL_로_리다이렉트된다")
    void redirectToProvider_redirectsToAuthorizationUrl() throws Exception {
        // given
        String provider = "google";
        String authorizationUrl = "https://google.com/oauth/authorize";

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(oauthAuthFacade.createOAuthAuthorizationUrl(provider))
                .thenReturn(authorizationUrl);

        // when
        oauthController.redirectToProvider(provider, response);

        // then
        verify(oauthAuthFacade).createOAuthAuthorizationUrl(provider);
        verify(response).sendRedirect(authorizationUrl);
    }

    @Test
    @DisplayName("OAuth_회원가입에_성공하면_OauthAuthResponseDto_를_반환한다")
    void signupWithOauth_returnsLoginResponse() {
        // given
        SignupOauthRequestDto requestDto = Mockito.mock(SignupOauthRequestDto.class);

        OauthAuthResult oauthAuthResult = OauthAuthResult.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        when(userSignupFacade.signupWithOauth(any())).thenReturn(oauthAuthResult);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = oauthController.signup(requestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());
        assertThat(response.getBody().getData().getAccessToken()).isEqualTo("access-token");

        HttpHeaders headers = response.getHeaders();
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
        assertThat(cookies).isNotNull();

        verify(userSignupFacade).signupWithOauth(any());
    }
}
