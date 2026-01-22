package teamdevhub.devhub.small.application.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.auth.AuthenticatedUserService;
import teamdevhub.devhub.domain.auth.RefreshToken;
import teamdevhub.devhub.domain.auth.vo.token.RefreshTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.fake.pure.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.repository.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AuthenticatedUserServiceTest {

    private AuthenticatedUserService authUserService;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeUserRepository userRepository;
    private FakeRefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        userRepository = new FakeUserRepository();
        refreshTokenRepository = new FakeRefreshTokenRepository();

        authUserService = new AuthenticatedUserService(tokenParseProvider, userRepository, refreshTokenRepository);
    }

    @Test
    @DisplayName("인증_인가_관련_사용자_정보를_조회한다")
    void fetchUserInfoForAuthentication() {
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

        UserCreateCommand generalUserCreateCommand = UserCreateCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

        userRepository.save(testUser);

        // when
        authUserService.getUserForLogin(testUser.getEmail());

        // then
        assertThat(userRepository.findAuthenticatedUserByEmail(testUser.getEmail()).userGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(userRepository.findAuthenticatedUserByEmail(testUser.getEmail()).userRole()).isEqualTo(testUser.getUserRole());
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

        UserCreateCommand generalUserCreateCommand = UserCreateCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);
        userRepository.save(testUser);

        RefreshToken refreshToken = new RefreshToken(testUser.getUserGuid(), REFRESH_TOKEN);
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN, new RefreshTokenInfo(testUser.getUserGuid()));
        refreshTokenRepository.givenRefreshToken(refreshToken);

        // when
        AuthenticatedUser reissueUser = authUserService.getUserForReissue(refreshToken.token());

        // then
        assertThat(reissueUser.userGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(reissueUser.userRole()).isEqualTo(testUser.getUserRole());
    }

}
