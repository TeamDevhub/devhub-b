package teamdevhub.devhub.small.application.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.application.service.auth.AuthenticationService;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.auth.RefreshToken;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.fake.pure.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.fake.pure.repository.auth.FakeRefreshTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    @DisplayName("액세스토큰을_재발급할_수_있다")
    void reissueAccessToken() {
        // given
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