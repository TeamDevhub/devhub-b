package teamdevhub.devhub.small.adapter.in.auth.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;

class EmailVerificationRequestDtoTest {

    @Test
    @DisplayName("EmailVerificationRequestDto_생성_후_email_이_정상적으로_들어가는지_확인할_수_있다")
    void canBindEmailAfterCreatingRequestDto() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto(TEST_EMAIL_1);

        // when, then
        assertThat(emailVerificationRequestDto).isNotNull();
        assertThat(emailVerificationRequestDto.getEmail()).isEqualTo(TEST_EMAIL_1);
    }
}