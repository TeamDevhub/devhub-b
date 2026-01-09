package teamdevhub.devhub.small.port.in.user.command;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

class SignupCommandTest {

    @Test
    void SignupRequestDto_를_SignupCommand_로_변환할_수_있다() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .username(TEST_USERNAME)
                .introduction(TEST_INTRO)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        SignupCommand signupCommand = SignupCommand.fromSignupUserRequestDto(signupRequestDto);

        // then
        assertThat(signupCommand.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(signupCommand.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(signupCommand.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(signupCommand.getIntroduction()).isEqualTo(TEST_INTRO);
        assertThat(signupCommand.getPositionList()).containsExactlyElementsOf(TEST_POSITION_LIST);
        assertThat(signupCommand.getSkillList()).containsExactlyElementsOf(TEST_SKILL_LIST);
    }

}