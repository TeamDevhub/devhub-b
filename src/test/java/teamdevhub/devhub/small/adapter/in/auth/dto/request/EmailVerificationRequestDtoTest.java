package teamdevhub.devhub.small.adapter.in.auth.dto.request;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerificationRequestDtoTest {

    @Test
    void EmailVerificationRequestDto_생성후_email_이_정상적으로_들어가는지_확인할_수_있다() {
        // given
        String email = "test@example.com";

        // when
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto(email);

        // then
        assertThat(emailVerificationRequestDto).isNotNull();
        assertThat(emailVerificationRequestDto.getEmail()).isEqualTo(email);
    }
}