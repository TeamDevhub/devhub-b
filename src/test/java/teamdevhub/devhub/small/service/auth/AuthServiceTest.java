package teamdevhub.devhub.small.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.domain.vo.auth.RefreshToken;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.service.auth.AuthService;
import teamdevhub.devhub.small.mock.provider.FakeAuthenticatedUserProvider;
import teamdevhub.devhub.small.mock.provider.FakeDateTimeProvider;
import teamdevhub.devhub.small.mock.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.small.mock.repository.FakeRefreshTokenRepository;
import teamdevhub.devhub.small.mock.usecase.FakeUserUseCase;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

class AuthServiceTest {

    private AuthService authService;

    private FakeTokenIssueProvider fakeTokenIssueProvider;
    private FakeAuthenticatedUserProvider fakeAuthenticatedUserProvider;
    private FakeUserUseCase fakeUserUseCase;
    private FakeRefreshTokenRepository fakeRefreshTokenRepository;

    @BeforeEach
    void init() {
        fakeTokenIssueProvider = new FakeTokenIssueProvider();
        fakeAuthenticatedUserProvider = new FakeAuthenticatedUserProvider();
        fakeUserUseCase = new FakeUserUseCase();
        fakeRefreshTokenRepository = new FakeRefreshTokenRepository();

        authService = new AuthService(
                fakeTokenIssueProvider,
                fakeAuthenticatedUserProvider,
                fakeUserUseCase,
                fakeRefreshTokenRepository
        );
    }

    @Test
    void 로그인을_하면_액세스토큰과_리프레시토큰이_발급된다() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL, TEST_PASSWORD);

        // when
        LoginResponseDto loginResponseDto = authService.login(loginCommand);

        // then
        assertThat(loginResponseDto).isNotNull();
        assertThat(loginResponseDto.getAccessToken()).isEqualTo("access-token-" + TEST_GUID);
        assertThat(loginResponseDto.getRefreshToken()).isEqualTo("refresh-token-" + TEST_GUID);
        assertThat(loginResponseDto.getPrefix()).isEqualTo("Bearer ");
    }

    @Test
    void 로그인을_하면_리프레시토큰이_저장된다() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL, TEST_PASSWORD);

        // when
        authService.login(loginCommand);

        // then
        RefreshToken refreshToken = fakeRefreshTokenRepository.findByUserGuid(TEST_GUID);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.token()).isEqualTo("refresh-token-" + TEST_GUID);
    }

    @Test
    void 로그인을_하면_마지막_로그인_시간이_업데이트된다() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL, TEST_PASSWORD);

        // when
        authService.login(loginCommand);

        // then
        assertThat(fakeUserUseCase.isLoginTimeUpdated(TEST_GUID)).isTrue();
    }

    @Test
    void 리프레시토큰으로_액세스토큰을_재발급할_수_있다() {
        // given
        String refreshToken = "refresh-token-" + TEST_GUID;
        fakeRefreshTokenRepository.save(
                RefreshToken.of(TEST_GUID, refreshToken)
        );

        // when
        TokenResponseDto response = authService.reissueAccessToken(refreshToken);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token-" + TEST_GUID);
    }

    @Test
    void 로그아웃을_하면_리프레시토큰이_삭제된다() {
        // given
        fakeRefreshTokenRepository.save(RefreshToken.of(TEST_GUID, "refresh-token-" + TEST_GUID));

        // when
        authService.revoke(TEST_GUID);

        // then
        assertThat(fakeRefreshTokenRepository.findByUserGuid(TEST_GUID)).isNull();
    }
}