package teamdevhub.devhub.small.port.in.user.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class SignupCommandTest {

    @Test
    @DisplayName("Builder_로_SignupCommand_를_생성할_수_있다")
    void convertRequestDtoToCommand() {
        // given, when
        SignupCommand signupCommand = SignupCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        // then
        assertThat(signupCommand.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(signupCommand.password()).isEqualTo(TEST_PASSWORD_1);
        assertThat(signupCommand.username()).isEqualTo(TEST_USERNAME_1);
        assertThat(signupCommand.introduction()).isEqualTo(TEST_INTRO_1);
        assertThat(signupCommand.positionList()).containsExactlyElementsOf(TEST_POSITION_LIST);
        assertThat(signupCommand.skillList()).containsExactlyElementsOf(TEST_SKILL_LIST);
    }
}