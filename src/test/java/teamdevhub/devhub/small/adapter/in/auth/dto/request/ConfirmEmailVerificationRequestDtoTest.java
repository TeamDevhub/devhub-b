package teamdevhub.devhub.small.adapter.in.auth.dto.request;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.request.ConfirmEmailVerificationRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

class ConfirmEmailVerificationRequestDtoTest {

    @Test
    void ConfirmEmailVerificationRequestDto_생성후_email_과_code_가_정상적으로_들어가는지_확인할_수_있다() {
        // given
        String email = "test@example.com";
        String code = "123456";

        // when
        ConfirmEmailVerificationRequestDto confirmEmailVerificationRequestDto = new ConfirmEmailVerificationRequestDto(email, code);

        // then
        assertThat(confirmEmailVerificationRequestDto).isNotNull();
        assertThat(confirmEmailVerificationRequestDto.getEmail()).isEqualTo(email);
        assertThat(confirmEmailVerificationRequestDto.getCode()).isEqualTo(code);
    }

}