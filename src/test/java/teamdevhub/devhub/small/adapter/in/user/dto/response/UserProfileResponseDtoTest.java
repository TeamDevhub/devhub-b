package teamdevhub.devhub.small.adapter.in.user.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.response.UserProfileResponseDto;
import teamdevhub.devhub.constant.TestConstant;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.audit.AuditInfo;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserProfileResponseDtoTest {

    @Test
    @DisplayName("UserProfileResponseDto_의_fromDomain_으로_모든_필드가_정상적으로_매핑된다")
    void bindAllFieldsFromDomainToResponseDto() {
        // given
        AuditInfo auditInfo = new AuditInfo(
                "creator",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                "modifier",
                LocalDateTime.of(2025, 1, 2, 15, 0)
        );

        User user = User.builder()
                .userGuid(TestConstant.TEST_GUID_1)
                .email(TestConstant.TEST_EMAIL_1)
                .password(TestConstant.TEST_PASSWORD_1)
                .userRole(UserRole.USER)
                .username(TestConstant.TEST_USERNAME_1)
                .introduction(TestConstant.TEST_INTRO_1)
                .positions(TestConstant.TEST_POSITIONS)
                .skills(TestConstant.TEST_SKILLS)
                .mannerDegree(TestConstant.TEST_MANNER)
                .blocked(TestConstant.TEST_BLOCKED)
                .blockEndDate(null)
                .deleted(TestConstant.TEST_DELETED)
                .lastLoginDateTime(TestConstant.TEST_LAST_LOGIN)
                .auditInfo(auditInfo)
                .build();

        // when
        UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.fromDomain(user);

        // then
        assertThat(userProfileResponseDto.getPassword()).isNull();
        assertThat(userProfileResponseDto.getUserRole()).isNull();
        assertThat(userProfileResponseDto.getUserGuid()).isEqualTo(TestConstant.TEST_GUID_1);
        assertThat(userProfileResponseDto.getEmail()).isEqualTo(TestConstant.TEST_EMAIL_1);
        assertThat(userProfileResponseDto.getUsername()).isEqualTo(TestConstant.TEST_USERNAME_1);
        assertThat(userProfileResponseDto.getIntroduction()).isEqualTo(TestConstant.TEST_INTRO_1);
        assertThat(userProfileResponseDto.getPositionList()).containsExactlyElementsOf(TestConstant.TEST_POSITION_LIST);
        assertThat(userProfileResponseDto.getSkillList()).containsExactlyElementsOf(TestConstant.TEST_SKILL_LIST);
        assertThat(userProfileResponseDto.getMannerDegree()).isEqualTo(TestConstant.TEST_MANNER);
        assertThat(userProfileResponseDto.isBlocked()).isEqualTo(TestConstant.TEST_BLOCKED);
        assertThat(userProfileResponseDto.getBlockEndDate()).isNull();
        assertThat(userProfileResponseDto.isDeleted()).isEqualTo(TestConstant.TEST_DELETED);
        assertThat(userProfileResponseDto.getLastLoginDateTime()).isEqualTo(TestConstant.TEST_LAST_LOGIN);
        assertThat(userProfileResponseDto.getCreatedBy()).isEqualTo(auditInfo.createdBy());
        assertThat(userProfileResponseDto.getCreatedAt()).isEqualTo(auditInfo.createdAt());
        assertThat(userProfileResponseDto.getModifiedBy()).isEqualTo(auditInfo.modifiedBy());
        assertThat(userProfileResponseDto.getModifiedAt()).isEqualTo(auditInfo.modifiedAt());
    }
}