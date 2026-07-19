package teamdevhub.devhub.small.core.auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.application.service.AuthenticationService;
import teamdevhub.devhub.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenIssueProvider;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    private FakeRefreshTokenRepository refreshTokenRepository;
    private FakeUserRepository userRepository;

    @BeforeEach
    void init() {
        FakeTokenIssueProvider fakeTokenIssueProvider = new FakeTokenIssueProvider();
        refreshTokenRepository = new FakeRefreshTokenRepository();
        userRepository = new FakeUserRepository();

        userRepository.givenUser(
                User.of(
                        TEST_USER_GUID_1,
                        UserRole.USER,
                        "tester",
                        null,
                        null,
                        36.5,
                        false,
                        null,
                        false,
                        null,
                        AuditInfo.empty()
                )
        );

        authenticationService = new AuthenticationService(
                fakeTokenIssueProvider,
                refreshTokenRepository,
                userRepository
        );
    }

    @Test
    @DisplayName("로그인을_하면_액세스토큰과_리프레시토큰이_발급된다")
    void issueAccessAndRefreshTokenWhenLogin() {
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        UserRole.USER
                );

        AuthResult authResult = authenticationService.login(authenticatedUser);

        assertThat(authResult).isNotNull();
        assertThat(authResult.accessToken())
                .isEqualTo("access-token-" + TEST_USER_GUID_1);
        assertThat(authResult.refreshToken())
                .isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("로그인을_하면_리프레시토큰이_저장된다")
    void storeRefreshTokenWhenLogin() {
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        UserRole.USER
                );

        authenticationService.login(authenticatedUser);

        Optional<RefreshToken> refreshToken =
                refreshTokenRepository.findByUserGuid(TEST_USER_GUID_1);

        assertThat(refreshToken)
                .isPresent()
                .get()
                .extracting(RefreshToken::token)
                .isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("OAuth_로그인을_하면_액세스토큰과_리프레시토큰이_발급된다")
    void issueAccessAndRefreshTokenWhenLoginWithOAuth() {
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        UserRole.USER
                );

        AuthResult authResult = authenticationService.login(authenticatedUser);

        assertThat(authResult).isNotNull();
        assertThat(authResult.accessToken())
                .isEqualTo("access-token-" + TEST_USER_GUID_1);
        assertThat(authResult.refreshToken())
                .isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("액세스토큰_재발급_시_새로운_리프레시토큰도_함께_발급된다")
    void reissueAccessToken_alsoRotatesRefreshToken() {
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        UserRole.USER
                );

        refreshTokenRepository.save(
                RefreshToken.of(TEST_USER_GUID_1, "old-refresh-token")
        );

        AuthResult authResult =
                authenticationService.reissueAccessToken(authenticatedUser);

        assertThat(authResult.accessToken())
                .isEqualTo("access-token-" + TEST_USER_GUID_1);

        assertThat(authResult.refreshToken())
                .isEqualTo("refresh-token-" + TEST_USER_GUID_1);

        assertThat(refreshTokenRepository.findByUserGuid(TEST_USER_GUID_1))
                .isPresent()
                .hasValueSatisfying(token ->
                        assertThat(token.token())
                                .isEqualTo("refresh-token-" + TEST_USER_GUID_1));
    }

    @Test
    @DisplayName("로그아웃을_하면_리프레시토큰이_삭제된다")
    void deleteRefreshTokenWhenLogout() {
        refreshTokenRepository.save(
                RefreshToken.of(
                        TEST_USER_GUID_1,
                        "refresh-token-" + TEST_USER_GUID_1
                )
        );

        authenticationService.revoke(TEST_USER_GUID_1);

        assertThat(refreshTokenRepository.contains(TEST_USER_GUID_1))
                .isFalse();
    }

    @Test
    @DisplayName("토큰은_전달받은_권한이_아닌_DB의_현재권한으로_발급된다")
    void login_usesCurrentRoleFromRepository() {
        userRepository.givenUser(
                User.of(
                        TEST_USER_GUID_1,
                        UserRole.ADMIN,
                        "admin",
                        null,
                        null,
                        36.5,
                        false,
                        null,
                        false,
                        null,
                        AuditInfo.empty()
                )
        );

        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        UserRole.USER
                );

        authenticationService.login(authenticatedUser);

        assertThat(userRepository.wasCalled("findByUserGuid"))
                .isTrue();

        assertThat(userRepository.callCount("findByUserGuid"))
                .isEqualTo(1);
    }
}