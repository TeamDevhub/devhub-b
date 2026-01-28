package teamdevhub.devhub.small.application.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.exception.BusinessRuleException;
import teamdevhub.devhub.core.auth.application.service.auth.AuthenticatedUserService;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.domain.RefreshToken;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.user.CreateUserCommand;
import teamdevhub.devhub.fake.pure.provider.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.repository.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AuthenticatedUserServiceTest {

    private AuthenticatedUserService authenticatedUserService;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeAuthenticatedUserResolver authenticatedUserResolver;
    private FakeUserRepository userRepository;
    private FakeRefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        authenticatedUserResolver = new FakeAuthenticatedUserResolver();
        userRepository = new FakeUserRepository();
        refreshTokenRepository = new FakeRefreshTokenRepository();

        authenticatedUserService = new AuthenticatedUserService(tokenParseProvider, authenticatedUserResolver, userRepository, refreshTokenRepository);
    }

    @Test
    @DisplayName("리프레시_토큰_재발급을_위해_사용자를_조회한다")
    void fetchUserForReissue() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);
        userRepository.save(testUser);

        RefreshToken refreshToken = new RefreshToken(testUser.getUserGuid(), REFRESH_TOKEN);
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN,testUser.getUserGuid());
        refreshTokenRepository.givenRefreshToken(refreshToken);

        // when
        AuthenticatedUser reissueUser = authenticatedUserService.getUserForReissue(refreshToken.token());

        // then
        assertThat(reissueUser.userGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(reissueUser.userRole()).isEqualTo(testUser.getUserRole());
    }


    @Test
    @DisplayName("저장되지 않은_리프레시_토큰으로_재발급을_요청하면_예외가_발생한다")
    void fetchUserForReissueWithInvalidTokenThrows() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        String invalidToken = "invalid-refresh-token";
        tokenParseProvider.givenRefreshToken(invalidToken,TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(() -> authenticatedUserService.getUserForReissue(invalidToken))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }
}
