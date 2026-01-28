package teamdevhub.devhub.small.core.auth.port.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;
import teamdevhub.devhub.core.auth.port.in.command.verification.IssueVerificationCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.VERIFICATION_TARGET_1;

public class IssueVerificationCommandTest {

    @Test
    @DisplayName("빌더로_IssueVerificationCommand_를_생성할_수_있다")
    void issueVerificationCommandBuilderWorks() {
        // given
        VerificationTarget verificationTarget = VERIFICATION_TARGET_1;

        // when
        IssueVerificationCommand issueVerificationCommand = IssueVerificationCommand.builder()
                .verificationTarget(verificationTarget)
                .build();

        // then
        assertThat(issueVerificationCommand.verificationTarget()).isEqualTo(verificationTarget);
    }
}
