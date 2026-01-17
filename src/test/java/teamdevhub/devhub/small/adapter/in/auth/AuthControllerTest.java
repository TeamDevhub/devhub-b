package teamdevhub.devhub.small.adapter.in.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.AuthController;
import teamdevhub.devhub.adapter.in.dto.request.auth.LoginRequestDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthSessionUseCase;
import teamdevhub.devhub.fake.pure.usecase.verification.FakeSignupVerificationUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthControllerTest {

    private AuthController authController;
    private FakeAuthSessionUseCase fakeAuthenticationUseCase;
    private FakeSignupVerificationUseCase fakeSignupVerificationUseCase;

    @BeforeEach
    void init() {
        fakeAuthenticationUseCase = new FakeAuthSessionUseCase();
        fakeSignupVerificationUseCase = new FakeSignupVerificationUseCase();
        authController = new AuthController(fakeAuthenticationUseCase, fakeSignupVerificationUseCase);
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
        assertThat(authController.login(loginRequestDto).getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());
        assertThat(authController.login(loginRequestDto).getBody()).isNotNull();
        assertThat(authController.login(loginRequestDto).getBody().getData().getAccessToken()).isEqualTo("access-token");
    }

    @Test
    @DisplayName("토큰_재발급에_성공하면_CREATE_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenRefreshingToken() {
        // given
        String refreshToken = "refresh-token";

        // when, then
        assertThat(authController.refresh(refreshToken).getBody().getCode()).isEqualTo(SuccessCode.CREATE_SUCCESS.getCode());
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
        assertThat(authController.revoke(authenticatedUser).getBody().getCode()).isEqualTo(SuccessCode.LOGOUT_SUCCESS.getCode());
        assertThat(fakeAuthenticationUseCase.getRevokedUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }
}