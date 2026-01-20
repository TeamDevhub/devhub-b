package teamdevhub.devhub.small.application.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.auth.AuthenticatedUserService;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.fake.pure.provider.FakePasswordPolicyProvider;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AuthUserServiceTest {

    private FakeUserRepository fakeUserRepository;

    private AuthenticatedUserService authUserService;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        authUserService = new AuthenticatedUserService(fakeUserRepository);
    }

    @Test
    @DisplayName("인증_인가_관련_사용자_정보를_조회한다")
    void fetchUserInfoForAuthentication() {
        // given
        SignupCommand signupCommand = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

        fakeUserRepository.save(testUser);

        // when
        authUserService.getUserForLogin(testUser.getEmail());

        // then
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(testUser.getEmail())).isNotNull();
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(testUser.getEmail()).userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(fakeUserRepository.findAuthenticatedUserByEmail(testUser.getEmail()).userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("리프레시_토큰_재발급을_위해_사용자를_조회한다")
    void fetchUserForReissue() {
        // given
        SignupCommand signupCommand = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);
        fakeUserRepository.save(testUser);

        // when
        AuthenticatedUser reissueUser = authUserService.getUserForReissue(testUser.getUserGuid());

        // then
        assertThat(reissueUser).isNotNull();
        assertThat(reissueUser.userGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(reissueUser.userRole()).isEqualTo(testUser.getUserRole());
    }

}
