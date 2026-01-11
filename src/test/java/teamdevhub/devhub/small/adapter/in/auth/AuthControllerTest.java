package teamdevhub.devhub.small.adapter.in.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.AuthController;
import teamdevhub.devhub.adapter.in.auth.dto.request.ConfirmEmailVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.LoginRequestDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.fake.pure.provider.FakeDateTimeProvider;
import teamdevhub.devhub.fake.pure.repository.FakeEmailVerificationRepository;
import teamdevhub.devhub.fake.pure.usecase.FakeAuthUseCase;
import teamdevhub.devhub.fake.pure.usecase.FakeEmailVerificationUseCase;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TestConstant.*;

class AuthControllerTest {

    private AuthController authController;
    private FakeAuthUseCase fakeAuthUseCase;
    private FakeEmailVerificationUseCase fakeEmailVerificationUseCase;

    @BeforeEach
    void init() {
        FakeDateTimeProvider fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        FakeEmailVerificationRepository fakeEmailVerificationRepository = new FakeEmailVerificationRepository(List.of(), fakeDateTimeProvider);
        fakeAuthUseCase = new FakeAuthUseCase();
        fakeEmailVerificationUseCase = new FakeEmailVerificationUseCase(fakeEmailVerificationRepository,fakeDateTimeProvider);
        authController = new AuthController(fakeAuthUseCase, fakeEmailVerificationUseCase);
    }

    @Test
    void 이메일_인증_메일_전송에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto(TEST_EMAIL);

        // when, then
        assertThat(authController.sendEmailVerification(emailVerificationRequestDto).getBody().getCode()).isEqualTo(SuccessCode.EMAIL_VERIFICATION_SENT.getCode());
    }

    @Test
    void 이메일_인증_확인에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        fakeEmailVerificationUseCase.sendEmailVerification(new EmailVerificationRequestDto(TEST_EMAIL));
        ConfirmEmailVerificationRequestDto confirmEmailVerificationRequestDto = new ConfirmEmailVerificationRequestDto(TEST_EMAIL, EMAIL_CODE);

        // when, then
        assertThat(authController.confirmEmailVerification(confirmEmailVerificationRequestDto).getBody().getCode()).isEqualTo(SuccessCode.EMAIL_VERIFICATION_SUCCESS.getCode());
    }

    @Test
    void 로그인에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        // when, then
        assertThat(authController.login(loginRequestDto).getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());
        assertThat(authController.login(loginRequestDto).getBody()).isNotNull();
        assertThat(authController.login(loginRequestDto).getBody().getData().getAccessToken()).isEqualTo("access-token");
    }

    @Test
    void 토큰_재발급에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        String refreshToken = "refresh-token";

        // when, then
        assertThat(authController.refresh(refreshToken).getBody().getCode()).isEqualTo(SuccessCode.CREATE_SUCCESS.getCode());
        assertThat(fakeAuthUseCase.getLastReissueRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void 로그아웃에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_GUID,
                TEST_EMAIL,
                TEST_PASSWORD,
                UserRole.USER
        );

        // when, then
        assertThat(authController.revoke(authenticatedUser).getBody().getCode()).isEqualTo(SuccessCode.LOGOUT_SUCCESS.getCode());
        assertThat(fakeAuthUseCase.getRevokedUserGuid()).isEqualTo(TEST_GUID);
    }
}