package teamdevhub.devhub.small.adapter.in.user.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TestConstant.*;

class SignupResponseDtoTest {

    @Test
    @DisplayName("User_도메인을_SignupResponseDto_로_변환할_수_있다")
    void convertDomainToResponseDto() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        SignupResponseDto signupResponseDto = SignupResponseDto.fromDomain(user);

        // then
        assertThat(signupResponseDto.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(signupResponseDto.getUsername()).isEqualTo(TEST_USERNAME_1);
    }
}