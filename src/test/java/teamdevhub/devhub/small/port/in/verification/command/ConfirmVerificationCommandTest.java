package teamdevhub.devhub.small.port.in.verification.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class ConfirmVerificationCommandTest {

    @Test
    @DisplayName("빌더로_ConfirmVerificationCommand_를_생성할_수_있다")
    void confirmVerificationCommandBuilderWorks() {
        // given
        VerificationTarget verificationTarget = VERIFICATION_TARGET_1;
        String code = "123456";

        // when
        ConfirmVerificationCommand confirmVerificationCommand = ConfirmVerificationCommand.builder()
                .verificationTarget(verificationTarget)
                .code(code)
                .build();

        // then
        assertThat(confirmVerificationCommand.verificationTarget()).isEqualTo(verificationTarget);
        assertThat(confirmVerificationCommand.code()).isEqualTo(code);
    }
}
