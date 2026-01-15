package teamdevhub.devhub.small.port.in.auth.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.dto.request.auth.LoginRequestDto;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class LoginCommandTest {

    @Test
    @DisplayName("LoginRequestDto_로부터_LoginCommand_를_생성할_수_있다")
    void createCommandFromRequestDto() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        // when
        LoginCommand loginCommand = LoginCommand.fromLoginRequestDto(loginRequestDto);

        // then
        assertThat(loginCommand.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(loginCommand.getPassword()).isEqualTo(TEST_PASSWORD_1);
    }

    @Test
    @DisplayName("빌더로_LoginCommand_를_생성할_수_있다")
    void createCommandWithBuilder() {
        // given, when
        LoginCommand loginCommand = LoginCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        // then
        assertThat(loginCommand.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(loginCommand.getPassword()).isEqualTo(TEST_PASSWORD_1);
    }

}