package teamdevhub.devhub.small.port.in.auth.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_PASSWORD_1;

class LoginCommandTest {

    @Test
    @DisplayName("빌더로_LoginCommand_를_생성할_수_있다")
    void createCommandWithBuilder() {
        // given, when
        LoginCommand loginCommand = LoginCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        // then
        assertThat(loginCommand.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(loginCommand.password()).isEqualTo(TEST_PASSWORD_1);
    }
}