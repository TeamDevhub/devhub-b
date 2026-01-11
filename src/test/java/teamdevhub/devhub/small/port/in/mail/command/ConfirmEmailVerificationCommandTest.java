package teamdevhub.devhub.small.port.in.mail.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.port.in.mail.command.ConfirmEmailVerificationCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TestConstant.EMAIL_CODE;
import static teamdevhub.devhub.constant.TestConstant.TEST_EMAIL_1;

class ConfirmEmailVerificationCommandTest {

    @Test
    @DisplayName("팩토리_메서드로_EmailVerificationCommand_를_생성할_수_있다")
    void createCommandWithFactoryMethod() {
        // given
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = ConfirmEmailVerificationCommand.of(TEST_EMAIL_1, EMAIL_CODE);

        // when, then
        assertThat(confirmEmailVerificationCommand.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(confirmEmailVerificationCommand.getCode()).isEqualTo(EMAIL_CODE);
    }

    @Test
    @DisplayName("생성자로_EmailVerificationCommand_를_생성할_수_있다")
    void createCommandWithConstructor() {
        // given
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = ConfirmEmailVerificationCommand.of(TEST_EMAIL_1, EMAIL_CODE);

        // when, then
        assertThat(confirmEmailVerificationCommand.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(confirmEmailVerificationCommand.getCode()).isEqualTo(EMAIL_CODE);
    }
}