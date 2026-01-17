package teamdevhub.devhub.small.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.dto.response.auth.LoginResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.auth.TokenResponseDto;
import teamdevhub.devhub.domain.auth.vo.RefreshToken;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.application.service.auth.AuthSessionService;
import teamdevhub.devhub.fake.pure.provider.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.fake.pure.repository.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.usecase.FakeUserProfileUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthServiceTest {

    private AuthSessionService authService;

    private FakeUserProfileUseCase fakeUserUseCase;
    private FakeRefreshTokenRepository fakeRefreshTokenRepository;

    @BeforeEach
    void init() {
        FakeTokenIssueProvider fakeTokenIssueProvider = new FakeTokenIssueProvider();
        FakeAuthenticatedUserResolver fakeAuthenticatedUserProvider = new FakeAuthenticatedUserResolver();
        fakeUserUseCase = new FakeUserProfileUseCase();
        fakeRefreshTokenRepository = new FakeRefreshTokenRepository();

        authService = new AuthSessionService(
                fakeTokenIssueProvider,
                fakeAuthenticatedUserProvider,
                fakeUserUseCase,
                fakeRefreshTokenRepository
        );
    }

    @Test
    @DisplayName("로그인을_하면_액세스토큰과_리프레시토큰이_발급된다")
    void issueAccessAndRefreshTokenWhenLogin() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        LoginResponseDto loginResponseDto = authService.login(loginCommand);

        // then
        assertThat(loginResponseDto).isNotNull();
        assertThat(loginResponseDto.getAccessToken()).isEqualTo("access-token-" + TEST_USER_GUID_1);
        assertThat(loginResponseDto.getRefreshToken()).isEqualTo("refresh-token-" + TEST_USER_GUID_1);
        assertThat(loginResponseDto.getPrefix()).isEqualTo("Bearer ");
    }

    @Test
    @DisplayName("로그인을_하면_리프레시토큰이_저장된다")
    void storeRefreshTokenWhenLogin() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        authService.login(loginCommand);

        // then
        RefreshToken refreshToken = fakeRefreshTokenRepository.findByUserGuid(TEST_USER_GUID_1);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.token()).isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("로그인을_하면_마지막_로그인_시간이_업데이트된다")
    void updateLastLoginDateTimeWhenLogin() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        authService.login(loginCommand);

        // then
        assertThat(fakeUserUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isTrue();
    }

    @Test
    @DisplayName("리프레시토큰으로_액세스토큰을_재발급할_수_있다")
    void refreshAccessTokenUsingRefreshToken() {
        // given
        String refreshToken = "refresh-token-" + TEST_USER_GUID_1;
        fakeRefreshTokenRepository.save(
                RefreshToken.of(TEST_USER_GUID_1, refreshToken)
        );

        // when
        TokenResponseDto response = authService.reissueAccessToken(refreshToken);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("로그아웃을_하면_리프레시토큰이_삭제된다")
    void deleteRefreshTokenWhenLogout() {
        // given
        fakeRefreshTokenRepository.save(RefreshToken.of(TEST_USER_GUID_1, "refresh-token-" + TEST_USER_GUID_1));

        // when
        authService.revoke(TEST_USER_GUID_1);

        // then
        assertThat(fakeRefreshTokenRepository.findByUserGuid(TEST_USER_GUID_1)).isNull();
    }
}