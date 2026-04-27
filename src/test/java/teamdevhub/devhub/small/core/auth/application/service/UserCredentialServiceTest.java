package teamdevhub.devhub.small.core.auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.UserCredentialService;
import teamdevhub.devhub.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeUserCredentialRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeEncodedPasswordProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserCredentialServiceTest {

    private UserCredentialService userCredentialService;
    private FakeUserCredentialRepository userCredentialRepository;
    private FakeRefreshTokenRepository refreshTokenRepository;
    private FakeTokenParseProvider tokenParseProvider;

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

    private SignupUserCommand signupCommand() {
        return SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
    }

    @Test
    @DisplayName("이메일_회원가입_시_사용자_GUID가_반환된다")
    void signupEmailUser_newEmail_returnsUserGuid() {
        // given
        SignupUserCommand command = signupCommand();

        // when
        String userGuid = userCredentialService.signupEmailUser(command);

        // then
        assertThat(userGuid).isEqualTo(TEST_USER_GUID_1);
        assertThat(userCredentialRepository.findEmailUserCredentialByEmail(TEST_EMAIL_1)).isPresent();
    }

    @Test
    @DisplayName("이미_가입된_이메일로_가입하면_예외가_발생한다")
    void signupEmailUser_duplicateEmail_throwsException() {
        // given
        userCredentialService.signupEmailUser(signupCommand());

        // when, then
        assertThatThrownBy(() -> userCredentialService.signupEmailUser(signupCommand()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.SIGNUP_FAIL.getMessage());
    }

    @Test
    @DisplayName("OAuth_회원가입_시_AuthenticatedUser가_반환된다")
    void signupOAuthUser_newOauthId_returnsAuthenticatedUser() {
        // given
        OauthUser oauthUser = OauthUser.builder()
                .oauthId(TEST_OAUTH_ID_1)
                .verificationProvider(VerificationProvider.GOOGLE)
                .email(TEST_EMAIL_1)
                .build();

        // when
        AuthenticatedUser result = userCredentialService.signupOAuthUser(oauthUser);

        // then
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userCredentialRepository.findOAuthUserCredentialByOAuth(VerificationProvider.GOOGLE, TEST_OAUTH_ID_1)).isPresent();
    }

    @Test
    @DisplayName("이미_가입된_OAuth_ID로_가입하면_예외가_발생한다")
    void signupOAuthUser_duplicateOauthId_throwsException() {
        // given
        OauthUser oauthUser = OauthUser.builder()
                .oauthId(TEST_OAUTH_ID_1)
                .verificationProvider(VerificationProvider.GOOGLE)
                .email(TEST_EMAIL_1)
                .build();
        userCredentialService.signupOAuthUser(oauthUser);

        // when, then
        assertThatThrownBy(() -> userCredentialService.signupOAuthUser(oauthUser))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.SIGNUP_FAIL.getMessage());
    }

    @Test
    @DisplayName("유효한_리프레시_토큰으로_재발급_요청하면_AuthenticatedUser가_반환된다")
    void getUserForReissue_validToken_returnsAuthenticatedUser() {
        // given
        userCredentialService.signupEmailUser(signupCommand());
        RefreshToken savedToken = new RefreshToken(TEST_USER_GUID_1, REFRESH_TOKEN);
        refreshTokenRepository.save(savedToken);
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN, TEST_USER_GUID_1);

        // when
        AuthenticatedUser result = userCredentialService.getUserForReissue(REFRESH_TOKEN);

        // then
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("저장되지_않은_리프레시_토큰으로_재발급_요청하면_예외가_발생한다")
    void getUserForReissue_tokenNotSaved_throwsException() {
        // given
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN, TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(() -> userCredentialService.getUserForReissue(REFRESH_TOKEN))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("저장된_토큰과_다른_토큰으로_재발급_요청하면_예외가_발생한다")
    void getUserForReissue_tokenMismatch_throwsException() {
        // given
        RefreshToken savedToken = new RefreshToken(TEST_USER_GUID_1, REFRESH_TOKEN);
        refreshTokenRepository.save(savedToken);
        tokenParseProvider.givenRefreshToken("different-token", TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(() -> userCredentialService.getUserForReissue("different-token"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("로그인_요청하면_인증된_사용자_정보가_반환된다")
    void authenticate_validCredentials_returnsAuthenticatedUser() {
        // given
        LoginCommand command = LoginCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        // when
        AuthenticatedUser result = userCredentialService.authenticate(command);

        // then
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.loginId()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("자격증명이_저장된_후_해당_사용자의_userGuid로_재발급이_가능하다")
    void getUserForReissue_afterSignup_userCredentialExists() {
        // given
        userCredentialService.signupEmailUser(signupCommand());
        RefreshToken savedToken = new RefreshToken(TEST_USER_GUID_1, REFRESH_TOKEN);
        refreshTokenRepository.save(savedToken);
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN, TEST_USER_GUID_1);

        // when
        AuthenticatedUser result = userCredentialService.getUserForReissue(REFRESH_TOKEN);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
    }
}
