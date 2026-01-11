package teamdevhub.devhub.small.port.in.user.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TestConstant.*;

class SignupCommandTest {

    @Test
    @DisplayName("SignupRequestDto_를_SignupCommand_로_변환할_수_있다")
    void convertRequestDtoToCommand() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        SignupCommand signupCommand = SignupCommand.fromSignupUserRequestDto(signupRequestDto);

        // then
        assertThat(signupCommand.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(signupCommand.getPassword()).isEqualTo(TEST_PASSWORD_1);
        assertThat(signupCommand.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(signupCommand.getIntroduction()).isEqualTo(TEST_INTRO_1);
        assertThat(signupCommand.getPositionList()).containsExactlyElementsOf(TEST_POSITION_LIST);
        assertThat(signupCommand.getSkillList()).containsExactlyElementsOf(TEST_SKILL_LIST);
    }

}