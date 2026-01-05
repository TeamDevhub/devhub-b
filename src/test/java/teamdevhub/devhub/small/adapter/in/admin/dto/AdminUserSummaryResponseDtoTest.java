package teamdevhub.devhub.small.adapter.in.admin.dto;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AdminUserSummaryResponseDtoTest {

    @Test
    void UserEntity_를_AdminUserSummaryResponseDto_로_변환한다() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        UserEntity userEntity = UserEntity.builder()
                .userGuid("GUID1")
                .email("user@example.com")
                .username("User")
                .introduction("Intro")
                .mannerDegree(4.5)
                .blocked(true)
                .blockEndDate(now.plusDays(7))
                .deleted(false)
                .lastLoginDt(now.minusDays(1))
                .build();

        //when
        AdminUserSummaryResponseDto dto = AdminUserSummaryResponseDto.fromEntity(userEntity);

        //then
        assertThat(dto.getUserGuid()).isEqualTo(userEntity.getUserGuid());
        assertThat(dto.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(dto.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(dto.getIntroduction()).isEqualTo(userEntity.getIntroduction());
        assertThat(dto.getMannerDegree()).isEqualTo(userEntity.getMannerDegree());

        assertThat(dto.isBlocked()).isEqualTo(userEntity.isBlocked());
        assertThat(dto.getBlockEndDate()).isEqualTo(userEntity.getBlockEndDate());
        assertThat(dto.isDeleted()).isEqualTo(userEntity.isDeleted());
        assertThat(dto.getLastLoginDateTime()).isEqualTo(userEntity.getLastLoginDt());
    }
}