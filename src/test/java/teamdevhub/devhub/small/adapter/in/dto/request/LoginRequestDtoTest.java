package teamdevhub.devhub.small.adapter.in.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.api.auth.adapter.in.model.request.LoginRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class LoginRequestDtoTest {

    @Test
    @DisplayName("LoginRequestDto_Builder_로_생성후_email_과_password_가_정상적으로_들어가는지_확인할_수_있다")
    void canBindFieldsAfterCreatingDtoWithBuilder() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        // when, then
        assertThat(loginRequestDto).isNotNull();
        assertThat(loginRequestDto.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(loginRequestDto.getPassword()).isEqualTo(TEST_PASSWORD_1);
    }

}