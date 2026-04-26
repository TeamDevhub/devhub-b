package teamdevhub.devhub.small.core.auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamdevhub.devhub.core.auth.application.service.UserCredentialService;
import teamdevhub.devhub.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeUserCredentialRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.application.provider.FakeEncodedPasswordProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthenticatedUserServiceTest {

    private UserCredentialService userCredentialService;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeUserCredentialRepository userCredentialRepository;
    private FakeRefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        userCredentialRepository = new FakeUserCredentialRepository();
        refreshTokenRepository = new FakeRefreshTokenRepository();

        userCredentialService = new UserCredentialService(
                tokenParseProvider,
                new FakeUuidIdentifierProvider(TEST_USER_GUID_1),
                new FakeEncodedPasswordProvider(),
                new FakeAuthenticatedUserResolver(),
                userCredentialRepository,
                refreshTokenRepository
        );
    }

    @Test
    @DisplayName("이메일_회원가입에_성공하면_userGuid_를_반환한다")
    void signupEmailUser_success_returns_userGuid() {
        // given
        SignupUserCommand command = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        // when
        String userGuid = userCredentialService.signupEmailUser(command);

        // then
        assertThat(userGuid).isEqualTo(TEST_USER_GUID_1);
        assertThat(userCredentialRepository.findEmailUserCredentialByEmail(TEST_EMAIL_1)).isPresent();
    }

    @Test
    @DisplayName("이메일_회원가입_후_자격증명이_저장된다")
    void signupEmailUser_credentialIsSaved() {
        // given
        SignupUserCommand command = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        // when
        userCredentialService.signupEmailUser(command);

        // then
        AuthenticatedUser saved = userCredentialRepository.findEmailUserCredentialByEmail(TEST_EMAIL_1).orElseThrow();
        assertThat(saved.loginId()).isEqualTo(TEST_EMAIL_1);
        assertThat(saved.userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("중복된_이메일로_회원가입_시_예외가_발생한다")
    void signupEmailUser_duplicateEmail_throwsException() {
        // given
        SignupUserCommand command = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        userCredentialService.signupEmailUser(command);

        // when, then
        assertThatThrownBy(() -> userCredentialService.signupEmailUser(command))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    @DisplayName("OAuth_회원가입에_성공하면_UserCredential_을_반환한다")
    void signupOAuthUser_success_returns_userCredential() {
        // given
        OauthUser oauthUser = new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        // when
        AuthenticatedUser result = userCredentialService.signupOAuthUser(oauthUser);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.loginId()).isEqualTo(TEST_OAUTH_ID_1);
    }

    @Test
    @DisplayName("중복된_OAuth_정보로_회원가입_시_예외가_발생한다")
    void signupOAuthUser_duplicate_throwsException() {
        // given
        OauthUser oauthUser = new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
        userCredentialService.signupOAuthUser(oauthUser);

        // when, then
        assertThatThrownBy(() -> userCredentialService.signupOAuthUser(oauthUser))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    @DisplayName("유효한_리프레시_토큰으로_UserCredential_을_조회한다")
    void getUserForReissue_validToken_returnsUserCredential() {
        // given
        SignupUserCommand command = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        userCredentialService.signupEmailUser(command);

        RefreshToken refreshToken = new RefreshToken(TEST_USER_GUID_1, REFRESH_TOKEN);
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN, TEST_USER_GUID_1);
        refreshTokenRepository.givenRefreshToken(refreshToken);

        // when
        AuthenticatedUser result = userCredentialService.getUserForReissue(REFRESH_TOKEN);

        // then
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.loginId()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("저장되지_않은_리프레시_토큰으로_재발급_요청하면_예외가_발생한다")
    void getUserForReissue_tokenNotSaved_throwsException() {
        // given: 토큰 파싱은 되지만 DB에 저장된 토큰이 없는 상황
        String invalidToken = "invalid-refresh-token";
        tokenParseProvider.givenRefreshToken(invalidToken, TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(() -> userCredentialService.getUserForReissue(invalidToken))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("저장된_토큰과_요청_토큰이_불일치하면_예외가_발생한다")
    void getUserForReissue_tokenMismatch_throwsException() {
        // given: 다른 토큰이 저장된 상황
        String storedToken = "stored-refresh-token";
        String requestToken = "different-refresh-token";

        tokenParseProvider.givenRefreshToken(requestToken, TEST_USER_GUID_1);
        refreshTokenRepository.givenRefreshToken(new RefreshToken(TEST_USER_GUID_1, storedToken));

        // when, then
        assertThatThrownBy(() -> userCredentialService.getUserForReissue(requestToken))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("이메일과_비밀번호로_인증하면_UserCredential_을_반환한다")
    void authenticate_success_returnsUserCredential() {
        // given
        LoginCommand loginCommand = LoginCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        // when
        AuthenticatedUser result = userCredentialService.authenticate(loginCommand);

        // then
        assertThat(result).isNotNull();
        assertThat(result.loginId()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
    }
}
