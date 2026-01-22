package teamdevhub.devhub.small.application.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.application.service.auth.AuthenticationService;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.auth.RefreshToken;
import teamdevhub.devhub.domain.auth.vo.token.RefreshTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.fake.pure.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.fake.pure.repository.auth.FakeRefreshTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    private FakeRefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void init() {
        FakeTokenIssueProvider fakeTokenIssueProvider = new FakeTokenIssueProvider();
        refreshTokenRepository = new FakeRefreshTokenRepository();

        authenticationService = new AuthenticationService(
                fakeTokenIssueProvider,
                refreshTokenRepository
        );
    }

    @Test
    @DisplayName("로그인을_하면_액세스토큰과_리프레시토큰이_발급된다")
    void issueAccessAndRefreshTokenWhenLogin() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        // when
        LoginResponseDto loginResponseDto = authenticationService.login(authenticatedUser);

        // then
        assertThat(loginResponseDto).isNotNull();
        assertThat(loginResponseDto.getAccessToken()).isEqualTo("access-token-" + TEST_USER_GUID_1);
        assertThat(loginResponseDto.getRefreshToken()).isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("로그인을_하면_리프레시토큰이_저장된다")
    void storeRefreshTokenWhenLogin() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        // when
        authenticationService.login(authenticatedUser);

        // then
        RefreshToken refreshToken = refreshTokenRepository.findByUserGuid(TEST_USER_GUID_1);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.token()).isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("OAuth_로그인을_하면_액세스토큰과_리프레시토큰이_발급된다")
    void issueAccessAndRefreshTokenWhenLoginWithOauth() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        // when
        LoginResponseDto loginResponseDto = authenticationService.loginWithOauth(authenticatedUser);

        // then
        assertThat(loginResponseDto).isNotNull();
        assertThat(loginResponseDto.getAccessToken()).isEqualTo("access-token-" + TEST_USER_GUID_1);
        assertThat(loginResponseDto.getRefreshToken()).isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    /**
     * 테스트케이스 수정 필요
    @Test
    @DisplayName("로그인을_하면_마지막_로그인_시간이_업데이트된다")
    void updateLastLoginDateTimeWhenLogin() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        // when
        authenticationService.login(authenticatedUser);

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
        refreshTokenRepository.save(RefreshToken.of(TEST_USER_GUID_1, refreshToken));

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        // when
        TokenResponseDto tokenResponseDto = authenticationService.reissueAccessToken(authenticatedUser);

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
        refreshTokenRepository.save(RefreshToken.of(TEST_USER_GUID_1, validRefreshToken));

        String invalidRefreshToken = "refresh-token-invalid-" + TEST_USER_GUID_1;
        RefreshTokenInfo refreshTokenInfo2 = new RefreshTokenInfo(TEST_USER_GUID_1);
        fakeTokenParseProvider.givenRefreshToken(invalidRefreshToken, refreshTokenInfo2);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                UserRole.USER
        );

        // when, then
        assertThatThrownBy(
                () -> authenticationService.reissueAccessToken(authenticatedUser))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("유효하지 않은 토큰입니다.");
    }
     */

    @Test
    @DisplayName("로그아웃을_하면_리프레시토큰이_삭제된다")
    void deleteRefreshTokenWhenLogout() {
        // given
        refreshTokenRepository.save(RefreshToken.of(TEST_USER_GUID_1, "refresh-token-" + TEST_USER_GUID_1));

        // when
        authenticationService.revoke(TEST_USER_GUID_1);

        // then
        assertThat(refreshTokenRepository.findByUserGuid(TEST_USER_GUID_1)).isNull();
    }
}