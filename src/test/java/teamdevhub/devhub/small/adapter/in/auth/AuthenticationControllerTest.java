package teamdevhub.devhub.small.adapter.in.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.authentication.AuthenticationController;
import teamdevhub.devhub.adapter.in.dto.request.auth.LoginRequestDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.AuthenticatedUser;
import teamdevhub.devhub.fake.pure.usecase.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.FakeSignupVerificationUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthenticationControllerTest {

    private AuthenticationController authenticationController;
    private FakeAuthenticationUseCase fakeAuthenticationUseCase;
    private FakeSignupVerificationUseCase fakeSignupVerificationUseCase;

    @BeforeEach
    void init() {
        fakeAuthenticationUseCase = new FakeAuthenticationUseCase();
        fakeSignupVerificationUseCase = new FakeSignupVerificationUseCase();
        authenticationController = new AuthenticationController(fakeAuthenticationUseCase, fakeSignupVerificationUseCase);
    }

//    @Test
//    @DisplayName("이메일_인증_메일_전송에_성공하면_EMAIL_VERIFICATION_SENT_CODE_를_확인할_수_있다")
//    void canVerifyCodeWhenSendingEmailVerification() {
//        // given
//        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto(TEST_EMAIL_1);
//
//        // when, then
//        assertThat(authController.sendEmailVerification(emailVerificationRequestDto).getBody().getCode()).isEqualTo(SuccessCode.EMAIL_VERIFICATION_SENT.getCode());
//    }
//
//    @Test
//    @DisplayName("이메일_인증_확인에_성공하면_EMAIL_VERIFICATION_SUCCESS_의_코드를_확인할_수_있다")
//    void canVerifyCodeWhenConfirmingEmailVerification() {
//        // given
//        fakeEmailVerificationUseCase.sendEmailVerification(new EmailVerificationRequestDto(TEST_EMAIL_1));
//        ConfirmEmailVerificationRequestDto confirmEmailVerificationRequestDto = new ConfirmEmailVerificationRequestDto(TEST_EMAIL_1, EMAIL_CODE);
//
//        // when, then
//        assertThat(authController.confirmEmailVerification(confirmEmailVerificationRequestDto).getBody().getCode()).isEqualTo(SuccessCode.EMAIL_VERIFICATION_SUCCESS.getCode());
//    }

    @Test
    @DisplayName("로그인에_성공하면_LOGIN_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenLoginSucceed() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        // when, then
        assertThat(authenticationController.login(loginRequestDto).getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());
        assertThat(authenticationController.login(loginRequestDto).getBody()).isNotNull();
        assertThat(authenticationController.login(loginRequestDto).getBody().getData().getAccessToken()).isEqualTo("access-token");
    }

    @Test
    @DisplayName("토큰_재발급에_성공하면_CREATE_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenRefreshingToken() {
        // given
        String refreshToken = "refresh-token";

        // when, then
        assertThat(authenticationController.refresh(refreshToken).getBody().getCode()).isEqualTo(SuccessCode.CREATE_SUCCESS.getCode());
        assertThat(fakeAuthenticationUseCase.getLastReissueRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("로그아웃에_성공하면_LOGOUT_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenLogoutSucceed() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        // when, then
        assertThat(authenticationController.revoke(authenticatedUser).getBody().getCode()).isEqualTo(SuccessCode.LOGOUT_SUCCESS.getCode());
        assertThat(fakeAuthenticationUseCase.getRevokedUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }
}