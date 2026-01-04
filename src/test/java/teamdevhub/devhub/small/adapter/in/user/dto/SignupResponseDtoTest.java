package teamdevhub.devhub.small.adapter.in.user.dto;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.domain.common.AuditInfo;
import teamdevhub.devhub.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SignupResponseDtoTest {

    private static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "password123!";
    private static final String TEST_USERNAME = "User";
    private static final String TEST_INTRO = "Hello World";
    private static final List<String> TEST_POSITION_LIST = List.of("001");
    private static final List<String> TEST_SKILL_LIST = List.of("001");
    private static final String TEST_CREATED_BY = "system";
    private static final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2025, 1, 1, 12, 0);
    private static final String TEST_MODIFIED_BY = "system";
    private static final LocalDateTime TEST_MODIFIED_AT = LocalDateTime.of(2025, 1, 1, 12, 0);

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