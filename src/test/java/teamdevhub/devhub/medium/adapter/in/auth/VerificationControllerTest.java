package teamdevhub.devhub.medium.adapter.in.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.adapter.in.auth.AuthFacade;
import teamdevhub.devhub.adapter.in.auth.controller.VerificationController;
import teamdevhub.devhub.adapter.in.auth.dto.request.ConfirmVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.IssueVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthCallbackResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_CODE;

public class VerificationControllerTest {

    private VerificationController verificationController;
    private AuthFacade authFacade;

    @BeforeEach
    void init() {
        authFacade = Mockito.mock(AuthFacade.class);
        verificationController = new VerificationController(authFacade);
    }

    @Test
    @DisplayName("이메일_인증_메일_전송에_성공하면_VERIFICATION_SENT_코드를_확인할_수_있다")
    void canVerifyCodeWhenSendingEmailVerification() {
        // given
        IssueVerificationRequestDto issueVerificationRequestDto = new IssueVerificationRequestDto(VerificationType.EMAIL, TEST_EMAIL_1);
        doNothing().when(authFacade).issueEmailVerification(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = verificationController.sendEmailVerification(issueVerificationRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode())
                .isEqualTo(SuccessCode.VERIFICATION_SENT.getCode());

        verify(authFacade).issueEmailVerification(any());
    }

    @Test
    @DisplayName("이메일_인증_확인에_성공하면_VERIFICATION_SUCCESS_코드를_확인할_수_있다")
    void canVerifyCodeWhenConfirmingEmailVerification() {
        // given
        ConfirmVerificationRequestDto confirmVerificationRequestDto = new ConfirmVerificationRequestDto(
                VerificationType.EMAIL,
                TEST_EMAIL_1,
                TEST_EMAIL_CODE
        );
        doNothing().when(authFacade).confirmEmailVerification(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = verificationController.confirmEmailVerification(confirmVerificationRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.VERIFICATION_SUCCESS.getCode());

        verify(authFacade).confirmEmailVerification(any());
    }

    @Test
    @DisplayName("OAuth_로그인_요청시_Provider_인증_URL_로_리다이렉트된다")
    void redirectToProvider_redirectsToAuthorizationUrl() throws Exception {
        // given
        String provider = "google";
        String authorizationUrl = "https://google.com/oauth/authorize";

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(authFacade.createOAuthAuthorizationUrl(provider))
                .thenReturn(authorizationUrl);

        // when
        verificationController.redirectToProvider(provider, response);

        // then
        verify(authFacade).createOAuthAuthorizationUrl(provider);
        verify(response).sendRedirect(authorizationUrl);
    }

    @Test
    @DisplayName("OAuth_콜백_처리_성공시_응답에_OauthCallbackResponseDto_를_포함한다")
    void handleOauthCallback_returnsCallbackResponse() {
        // given
        String provider = "github";
        String code = "authorization-code";
        OauthCallbackResponseDto callbackResponse = OauthCallbackResponseDto.existedUser("TEMP_TOKEN");

        Mockito.when(authFacade.handleOAuthCallback(provider, code))
                .thenReturn(callbackResponse);

        // when
        ResponseEntity<DataApiResponseDto<OauthCallbackResponseDto>> response = verificationController.handleOauthCallback(provider, code);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.CREATE_SUCCESS.getCode());
        assertThat(response.getBody().getData()).isEqualTo(callbackResponse);

        verify(authFacade).handleOAuthCallback(provider, code);
    }


}
