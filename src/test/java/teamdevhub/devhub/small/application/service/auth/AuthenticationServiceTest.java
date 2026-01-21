package teamdevhub.devhub.small.application.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.application.service.auth.AuthenticationService;
import teamdevhub.devhub.domain.auth.RefreshToken;
import teamdevhub.devhub.domain.auth.vo.token.RefreshTokenInfo;
import teamdevhub.devhub.fake.pure.provider.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.fake.pure.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.repository.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticatedUserUseCase;
import teamdevhub.devhub.fake.pure.usecase.user.FakeUserLoginUseCase;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthenticationServiceTest {

    private AuthenticationService authSessionService;
    private FakeTokenParseProvider fakeTokenParseProvider;
    private FakeUserLoginUseCase fakeUserLoginUseCase;
    private FakeRefreshTokenRepository fakeRefreshTokenRepository;

    @BeforeEach
    void init() {
        FakeTokenIssueProvider fakeTokenIssueProvider = new FakeTokenIssueProvider();
        fakeTokenParseProvider = new FakeTokenParseProvider();
        FakeAuthenticatedUserResolver fakeAuthenticatedUserResolver = new FakeAuthenticatedUserResolver();
        FakeAuthenticatedUserUseCase fakeAuthUserUseCase = new FakeAuthenticatedUserUseCase();
        fakeUserLoginUseCase = new FakeUserLoginUseCase();
        fakeRefreshTokenRepository = new FakeRefreshTokenRepository();

        authSessionService = new AuthenticationService(
                fakeTokenIssueProvider,
                fakeTokenParseProvider,
                fakeAuthenticatedUserResolver,
                fakeAuthUserUseCase,
                fakeUserLoginUseCase,
                fakeRefreshTokenRepository
        );
    }

    @Test
    @DisplayName("로그인을_하면_액세스토큰과_리프레시토큰이_발급된다")
    void issueAccessAndRefreshTokenWhenLogin() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        LoginResponseDto loginResponseDto = authSessionService.login(loginCommand);

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
        authSessionService.login(loginCommand);

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
        authSessionService.login(loginCommand);

        // then
        assertThat(fakeUserLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isTrue();
    }

    @Test
    @DisplayName("리프레시토큰으로_액세스토큰을_재발급할_수_있다")
    void refreshAccessTokenUsingRefreshToken() {
        // given
        String refreshToken = "refresh-token-" + TEST_USER_GUID_1;
        RefreshTokenInfo refreshTokenInfo = new RefreshTokenInfo(TEST_USER_GUID_1);
        fakeTokenParseProvider.givenRefreshToken(refreshToken, refreshTokenInfo);
        fakeRefreshTokenRepository.save(RefreshToken.of(TEST_USER_GUID_1, refreshToken));

        // when
        TokenResponseDto tokenResponseDto = authSessionService.reissueAccessToken(refreshToken);

        // then
        assertThat(tokenResponseDto).isNotNull();
        assertThat(tokenResponseDto.getAccessToken()).isEqualTo("access-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("저장된_리프레시토큰과_다른_토큰으로_재발급하면_예외가_발생한다")
    void reissueAccessTokenWithInvalidTokenThrows() {
        // given
        String validRefreshToken = "refresh-token-" + TEST_USER_GUID_1;
        RefreshTokenInfo refreshTokenInfo1 = new RefreshTokenInfo(TEST_USER_GUID_1);
        fakeTokenParseProvider.givenRefreshToken(validRefreshToken, refreshTokenInfo1);
        fakeRefreshTokenRepository.save(RefreshToken.of(TEST_USER_GUID_1, validRefreshToken));

        String invalidRefreshToken = "refresh-token-invalid-" + TEST_USER_GUID_1;
        RefreshTokenInfo refreshTokenInfo2 = new RefreshTokenInfo(TEST_USER_GUID_1);
        fakeTokenParseProvider.givenRefreshToken(invalidRefreshToken, refreshTokenInfo2);

        // when, then
        assertThatThrownBy(
                () -> authSessionService.reissueAccessToken(invalidRefreshToken))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("유효하지 않은 토큰입니다.");
    }

    @Test
    @DisplayName("로그아웃을_하면_리프레시토큰이_삭제된다")
    void deleteRefreshTokenWhenLogout() {
        // given
        fakeRefreshTokenRepository.save(RefreshToken.of(TEST_USER_GUID_1, "refresh-token-" + TEST_USER_GUID_1));

        // when
        authSessionService.revoke(TEST_USER_GUID_1);

        // then
        assertThat(fakeRefreshTokenRepository.findByUserGuid(TEST_USER_GUID_1)).isNull();
    }
}