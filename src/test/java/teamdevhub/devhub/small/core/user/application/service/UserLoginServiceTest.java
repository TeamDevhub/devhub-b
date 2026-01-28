package teamdevhub.devhub.small.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.user.application.service.UserLoginService;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserLoginServiceTest {

    private UserLoginService userLoginService;

    private FakeUserRepository userRepository;
    private FakeTimeProvider timeProvider;

    @BeforeEach
    void init() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        userRepository = new FakeUserRepository();

        userLoginService = new UserLoginService(timeProvider, userRepository);
    }

    @Test
    @DisplayName("로그인을_하면_최종_로그인_일시가_변한다")
    void updateLastLoginDateWhenLogin() {
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

        // when
        userLoginService.updateLastLoginDateTime(TEST_USER_GUID_1);

        // then
        assertThat(userRepository.wasCalled("updateLastLoginDateTime")).isTrue();
        assertThat(userRepository.callCount("updateLastLoginDateTime")).isEqualTo(1);
        assertThat(userRepository.lastLoginOf(testUser.getUserGuid())).isEqualTo(timeProvider.now());
    }
}
