package teamdevhub.devhub.small.adapter.in.admin.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.admin.user.dto.UserBasicResponseDto;
import teamdevhub.devhub.domain.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserBasicResponseDtoTest {

    @Test
    @DisplayName("User_를_UserBasicResponseDto_로_변환한다")
    void convertDomainToResponseDto() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        User user = User.builder()
                .userGuid(TEST_USER_GUID_1)
                .password(TEST_PASSWORD_1)
                .email(TEST_EMAIL_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .mannerDegree(TEST_MANNER)
                .blocked(true)
                .blockEndDate(now.plusDays(7))
                .deleted(false)
                .lastLoginDateTime(now.minusDays(1))
                .build();

        // when
        UserBasicResponseDto userBasicResponseDto = UserBasicResponseDto.fromDomain(user);

        // then
        assertThat(userBasicResponseDto.getUserGuid()).isEqualTo(user.getUserGuid());
        assertThat(userBasicResponseDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userBasicResponseDto.getUsername()).isEqualTo(user.getUsername());
        assertThat(userBasicResponseDto.getIntroduction()).isEqualTo(user.getIntroduction());
        assertThat(userBasicResponseDto.getMannerDegree()).isEqualTo(user.getMannerDegree());
        assertThat(userBasicResponseDto.isBlocked()).isEqualTo(user.isBlocked());
        assertThat(userBasicResponseDto.getBlockEndDate()).isEqualTo(user.getBlockEndDate());
        assertThat(userBasicResponseDto.isDeleted()).isEqualTo(user.isDeleted());
        assertThat(userBasicResponseDto.getLastLoginDateTime()).isEqualTo(user.getLastLoginDateTime());
    }
}