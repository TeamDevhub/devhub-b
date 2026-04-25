package teamdevhub.devhub.medium.outbound.auth.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.outbound.auth.adapter.UserCredentialAdapter;
import teamdevhub.devhub.outbound.auth.persistence.JpaEmailCredentialRepository;
import teamdevhub.devhub.outbound.auth.persistence.JpaOauthCredentialRepository;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest
@Transactional
class AuthenticatedUserAdapterTest {

    @Autowired
    private UserCredentialAdapter userCredentialAdapter;

    @Autowired
    private JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @Autowired
    private JpaOauthCredentialRepository jpaOauthCredentialRepository;

    @BeforeEach
    void init() {
        jpaEmailCredentialRepository.deleteAll();
        jpaOauthCredentialRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일_자격증명을_저장하면_DB에_저장된다")
    void saveEmailUserCredential_savedToDb() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);

        // when
        userCredentialAdapter.saveEmailUserCredential(authenticatedUser, TEST_PASSWORD_1);

        // then
        assertThat(jpaEmailCredentialRepository.findByEmail(TEST_EMAIL_1)).isPresent();
    }

    @Test
    @DisplayName("이메일로_자격증명을_조회한다")
    void findEmailUserCredentialByEmail_found() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        userCredentialAdapter.saveEmailUserCredential(authenticatedUser, TEST_PASSWORD_1);

        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findEmailUserCredentialByEmail(TEST_EMAIL_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().loginId()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.get().userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.get().userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("존재하지_않는_이메일로_조회하면_Optional_empty_를_반환한다")
    void findEmailUserCredentialByEmail_notFound_returnsEmpty() {
        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findEmailUserCredentialByEmail("notexist@email.com");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("userGuid_로_이메일_자격증명을_조회한다")
    void findEmailUserCredentialByUserGuid_found() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        userCredentialAdapter.saveEmailUserCredential(authenticatedUser, TEST_PASSWORD_1);

        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findEmailUserCredentialByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.get().loginId()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("존재하지_않는_userGuid_로_조회하면_Optional_empty_를_반환한다")
    void findEmailUserCredentialByUserGuid_notFound_returnsEmpty() {
        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findEmailUserCredentialByUserGuid("NOT_EXIST_GUID");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("OAuth_자격증명을_저장하면_DB에_저장된다")
    void saveOAuthUserCredential_savedToDb() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);

        // when
        userCredentialAdapter.saveOAuthUserCredential(authenticatedUser, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        // then
        assertThat(jpaOauthCredentialRepository.findByProviderAndOauthId(VerificationProvider.GOOGLE, TEST_OAUTH_ID_1)).isPresent();
    }

    @Test
    @DisplayName("OAuth_제공자와_oauthId_로_자격증명을_조회한다")
    void findOAuthUserCredentialByOAuth_found() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        userCredentialAdapter.saveOAuthUserCredential(authenticatedUser, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findOAuthUserCredentialByOAuth(VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("존재하지_않는_OAuth_정보로_조회하면_Optional_empty_를_반환한다")
    void findOAuthUserCredentialByOAuth_notFound_returnsEmpty() {
        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findOAuthUserCredentialByOAuth(VerificationProvider.GOOGLE, "not-exist-id");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("다른_제공자의_동일한_oauthId_는_별도로_저장된다")
    void saveOAuthUserCredential_differentProviders_storedSeparately() {
        // given
        AuthenticatedUser googleCredential = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        AuthenticatedUser githubCredential = AuthenticatedUser.of(TEST_USER_GUID_2, TEST_EMAIL_2, UserRole.USER);

        userCredentialAdapter.saveOAuthUserCredential(googleCredential, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);
        userCredentialAdapter.saveOAuthUserCredential(githubCredential, VerificationProvider.GITHUB, TEST_OAUTH_ID_1);

        // when
        Optional<AuthenticatedUser> google = userCredentialAdapter.findOAuthUserCredentialByOAuth(VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);
        Optional<AuthenticatedUser> github = userCredentialAdapter.findOAuthUserCredentialByOAuth(VerificationProvider.GITHUB, TEST_OAUTH_ID_1);

        // then
        assertThat(google).isPresent();
        assertThat(github).isPresent();
        assertThat(google.get().userGuid()).isNotEqualTo(github.get().userGuid());
    }
}
