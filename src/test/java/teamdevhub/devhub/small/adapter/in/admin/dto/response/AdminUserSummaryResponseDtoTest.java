package teamdevhub.devhub.small.adapter.in.admin.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AdminUserSummaryResponseDtoTest {

    @Test
    @DisplayName("UserEntity_를_AdminUserSummaryResponseDto_로_변환한다")
    void convertEntityToResponseDto() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        UserEntity userEntity = UserEntity.builder()
                .userGuid(TEST_USER_GUID_1)
                .email(TEST_EMAIL_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .mannerDegree(TEST_MANNER)
                .blocked(true)
                .blockEndDate(now.plusDays(7))
                .deleted(false)
                .lastLoginDt(now.minusDays(1))
                .build();

        // when
        AdminUserSummaryResponseDto adminUserSummaryResponseDto = AdminUserSummaryResponseDto.fromEntity(userEntity);

        // then
        assertThat(adminUserSummaryResponseDto.getUserGuid()).isEqualTo(userEntity.getUserGuid());
        assertThat(adminUserSummaryResponseDto.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(adminUserSummaryResponseDto.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(adminUserSummaryResponseDto.getIntroduction()).isEqualTo(userEntity.getIntroduction());
        assertThat(adminUserSummaryResponseDto.getMannerDegree()).isEqualTo(userEntity.getMannerDegree());
        assertThat(adminUserSummaryResponseDto.isBlocked()).isEqualTo(userEntity.isBlocked());
        assertThat(adminUserSummaryResponseDto.getBlockEndDate()).isEqualTo(userEntity.getBlockEndDate());
        assertThat(adminUserSummaryResponseDto.isDeleted()).isEqualTo(userEntity.isDeleted());
        assertThat(adminUserSummaryResponseDto.getLastLoginDateTime()).isEqualTo(userEntity.getLastLoginDt());
    }
}