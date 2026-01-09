package teamdevhub.devhub.small.adapter.in.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.adapter.in.auth.AuthController;
import teamdevhub.devhub.adapter.in.auth.dto.request.ConfirmEmailVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.LoginRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataResponseDto;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.small.mock.provider.FakeDateTimeProvider;
import teamdevhub.devhub.small.mock.repository.FakeEmailVerificationRepository;
import teamdevhub.devhub.small.mock.usecase.FakeAuthUseCase;
import teamdevhub.devhub.small.mock.usecase.FakeEmailVerificationUseCase;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

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

        // when
        ResponseEntity<ApiDataResponseDto<Void>> response = authController.sendEmailVerification(emailVerificationRequestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 이메일_인증_확인에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        fakeEmailVerificationUseCase.sendEmailVerification(new EmailVerificationRequestDto(TEST_EMAIL));
        ConfirmEmailVerificationRequestDto confirmEmailVerificationRequestDto = new ConfirmEmailVerificationRequestDto(TEST_EMAIL, EMAIL_CODE);

        // when
        ResponseEntity<ApiDataResponseDto<Void>> response = authController.confirmEmailVerification(confirmEmailVerificationRequestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 로그인에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        // when
        ResponseEntity<ApiDataResponseDto<TokenResponseDto>> response = authController.login(loginRequestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getAccessToken()).isEqualTo("access-token");
    }

    @Test
    void 토큰_재발급에_성공하면_HTTPSTATUS_OK_를_반환한다() {
        // given
        String refreshToken = "refresh-token";

        // when
        ResponseEntity<ApiDataResponseDto<TokenResponseDto>> response = authController.refresh(refreshToken);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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

        // when
        ResponseEntity<ApiDataResponseDto<Void>> response = authController.revoke(authenticatedUser);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fakeAuthUseCase.getRevokedUserGuid()).isEqualTo(TEST_GUID);
    }
}