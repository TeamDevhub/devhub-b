package teamdevhub.devhub.small.core.auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.AuthenticationService;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeRefreshTokenRepository;

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
                TEST_EMAIL_1,
                UserRole.USER
        );

        // when
        AuthResult authResult = authenticationService.login(authenticatedUser);

        // then
        assertThat(authResult).isNotNull();
        assertThat(authResult.accessToken()).isEqualTo("access-token-" + TEST_USER_GUID_1);
        assertThat(authResult.refreshToken()).isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("로그인을_하면_리프레시토큰이_저장된다")
    void storeRefreshTokenWhenLogin() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
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
                TEST_EMAIL_1,
                UserRole.USER
        );

        // when
        AuthResult authResult = authenticationService.login(authenticatedUser);

        // then
        assertThat(authResult).isNotNull();
        assertThat(authResult.accessToken()).isEqualTo("access-token-" + TEST_USER_GUID_1);
        assertThat(authResult.refreshToken()).isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("액세스토큰을_재발급할_수_있다")
    void reissueAccessToken() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );

        // when
        AuthResult authResult = authenticationService.reissueAccessToken(authenticatedUser);

        // then
        assertThat(authResult).isNotNull();
        assertThat(authResult.accessToken()).isEqualTo("access-token-" + TEST_USER_GUID_1);
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