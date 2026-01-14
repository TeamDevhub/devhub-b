package teamdevhub.devhub.small.adapter.in.user.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.response.UserProfileResponseDto;
import teamdevhub.devhub.constant.UserTestConstant;
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
                .userGuid(UserTestConstant.TEST_USER_GUID_1)
                .email(UserTestConstant.TEST_EMAIL_1)
                .password(UserTestConstant.TEST_PASSWORD_1)
                .userRole(UserRole.USER)
                .username(UserTestConstant.TEST_USERNAME_1)
                .introduction(UserTestConstant.TEST_INTRO_1)
                .positions(UserTestConstant.TEST_USER_POSITIONS)
                .skills(UserTestConstant.TEST_USER_SKILLS)
                .mannerDegree(UserTestConstant.TEST_MANNER)
                .blocked(UserTestConstant.TEST_BLOCKED)
                .blockEndDate(null)
                .deleted(UserTestConstant.TEST_DELETED)
                .lastLoginDateTime(UserTestConstant.TEST_LAST_LOGIN)
                .auditInfo(auditInfo)
                .build();

        // when
        UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.fromDomain(user);

        // then
        assertThat(userProfileResponseDto.getPassword()).isNull();
        assertThat(userProfileResponseDto.getUserRole()).isNull();
        assertThat(userProfileResponseDto.getUserGuid()).isEqualTo(UserTestConstant.TEST_USER_GUID_1);
        assertThat(userProfileResponseDto.getEmail()).isEqualTo(UserTestConstant.TEST_EMAIL_1);
        assertThat(userProfileResponseDto.getUsername()).isEqualTo(UserTestConstant.TEST_USERNAME_1);
        assertThat(userProfileResponseDto.getIntroduction()).isEqualTo(UserTestConstant.TEST_INTRO_1);
        assertThat(userProfileResponseDto.getPositionList()).containsExactlyElementsOf(UserTestConstant.TEST_POSITION_LIST);
        assertThat(userProfileResponseDto.getSkillList()).containsExactlyElementsOf(UserTestConstant.TEST_SKILL_LIST);
        assertThat(userProfileResponseDto.getMannerDegree()).isEqualTo(UserTestConstant.TEST_MANNER);
        assertThat(userProfileResponseDto.isBlocked()).isEqualTo(UserTestConstant.TEST_BLOCKED);
        assertThat(userProfileResponseDto.getBlockEndDate()).isNull();
        assertThat(userProfileResponseDto.isDeleted()).isEqualTo(UserTestConstant.TEST_DELETED);
        assertThat(userProfileResponseDto.getLastLoginDateTime()).isEqualTo(UserTestConstant.TEST_LAST_LOGIN);
        assertThat(userProfileResponseDto.getCreatedBy()).isEqualTo(auditInfo.createdBy());
        assertThat(userProfileResponseDto.getCreatedAt()).isEqualTo(auditInfo.createdAt());
        assertThat(userProfileResponseDto.getModifiedBy()).isEqualTo(auditInfo.modifiedBy());
        assertThat(userProfileResponseDto.getModifiedAt()).isEqualTo(auditInfo.modifiedAt());
    }
}