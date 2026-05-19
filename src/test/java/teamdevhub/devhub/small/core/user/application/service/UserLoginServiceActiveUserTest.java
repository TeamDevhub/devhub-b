package teamdevhub.devhub.small.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.user.application.service.UserLoginService;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserLoginServiceActiveUserTest {

    private UserLoginService userLoginService;
    private FakeUserRepository userRepository;
    private FakeTimeProvider timeProvider;

    @BeforeEach
    void init() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 6, 1, 9, 0));
        userRepository = new FakeUserRepository();
        userLoginService = new UserLoginService(timeProvider, userRepository);

        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST).verificationTarget(VERIFICATION_TARGET_1).build();
        User user = User.createGeneralUser(
                CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1));
        userRepository.save(user);
    }

    @Test
    @DisplayName("활성_상태의_사용자가_로그인을_시도하면_예외가_발생하지_않는다")
    void validateLoginUser_activeUser_noException() {
        // when, then
        assertThatCode(() -> userLoginService.validateLoginUser(TEST_USER_GUID_1))
                .doesNotThrowAnyException();
    }
}
