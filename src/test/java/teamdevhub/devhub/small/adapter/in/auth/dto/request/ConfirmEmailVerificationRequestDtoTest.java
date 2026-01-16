package teamdevhub.devhub.small.adapter.in.auth.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class ConfirmEmailVerificationRequestDtoTest {

    @Test
    @DisplayName("ConfirmEmailVerificationRequestDto_생성_후_email_과_code_가_정상적으로_들어가는지_확인할_수_있다")
    void canBindFieldsAfterCreatingRequestDto() {
        // given
        ConfirmEmailVerificationRequestDto confirmEmailVerificationRequestDto = new ConfirmEmailVerificationRequestDto(TEST_EMAIL_1, EMAIL_CODE);

        // when, then
        assertThat(confirmEmailVerificationRequestDto).isNotNull();
        assertThat(confirmEmailVerificationRequestDto.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(confirmEmailVerificationRequestDto.getCode()).isEqualTo(EMAIL_CODE);
    }

}