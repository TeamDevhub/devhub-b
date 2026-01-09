package teamdevhub.devhub.small.port.in.mail.command;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.port.in.mail.command.ConfirmEmailVerificationCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.EMAIL_CODE;
import static teamdevhub.devhub.small.mock.constant.TestConstant.TEST_EMAIL;

class ConfirmEmailVerificationCommandTest {

    @Test
    void 팩토리_메서드_로_객체를_생성할_수_있다() {
        // given
        String email = TEST_EMAIL;
        String code = EMAIL_CODE;

        // when
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = ConfirmEmailVerificationCommand.of(email, code);

        // then
        assertThat(confirmEmailVerificationCommand.getEmail()).isEqualTo(email);
        assertThat(confirmEmailVerificationCommand.getCode()).isEqualTo(code);
    }

    @Test
    void 생성자로_객체를_생성할_수_있다() {
        // given
        String email = TEST_EMAIL;
        String code = EMAIL_CODE;

        // when
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = ConfirmEmailVerificationCommand.of(email, code);


        // then
        assertThat(confirmEmailVerificationCommand.getEmail()).isEqualTo(email);
        assertThat(confirmEmailVerificationCommand.getCode()).isEqualTo(code);
    }
}