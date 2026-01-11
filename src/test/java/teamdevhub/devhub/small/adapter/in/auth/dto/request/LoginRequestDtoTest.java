package teamdevhub.devhub.small.adapter.in.auth.dto.request;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.request.LoginRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestDtoTest {

    @Test
    void LoginRequestDto_Builder_로_생성후_email_과_password_가_정상적으로_들어가는지_확인할_수_있다() {
        // given
        String email = "user@example.com";
        String password = "passw0rd!";

        // when
        LoginRequestDto dto = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getEmail()).isEqualTo(email);
        assertThat(dto.getPassword()).isEqualTo(password);
    }

}