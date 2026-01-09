package teamdevhub.devhub.small.adapter.in.user.dto;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;

class SignupResponseDtoTest {

    @Test
    void User_도메인을_SignupResponseDto_로_변환할_수_있다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        SignupResponseDto signupResponseDto = SignupResponseDto.fromDomain(user);

        // then
        assertThat(signupResponseDto.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(signupResponseDto.getUsername()).isEqualTo(TEST_USERNAME);
    }
}