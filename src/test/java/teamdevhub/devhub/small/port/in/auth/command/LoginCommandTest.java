package teamdevhub.devhub.small.port.in.auth.command;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.request.LoginRequestDto;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

class LoginCommandTest {

    @Test
    void LoginRequestDto_로부터_LoginCommand_를_생성할_수_있다() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        // when
        LoginCommand loginCommand = LoginCommand.fromLoginRequestDto(loginRequestDto);

        // then
        assertThat(loginCommand.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(loginCommand.getPassword()).isEqualTo(TEST_PASSWORD);
    }

    @Test
    void 빌더로_LoginCommand_를_생성할_수_있다() {
        // given, when
        LoginCommand loginCommand = LoginCommand.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        // then
        assertThat(loginCommand.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(loginCommand.getPassword()).isEqualTo(TEST_PASSWORD);
    }

}