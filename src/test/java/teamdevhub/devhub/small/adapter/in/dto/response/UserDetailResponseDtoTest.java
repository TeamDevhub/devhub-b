package teamdevhub.devhub.small.adapter.in.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.infrastructure.user.adapter.in.dto.response.UserDetailResponseDto;
import teamdevhub.devhub.constant.UserTestConstant;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.common.audit.AuditInfo;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailResponseDtoTest {

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
                .lastLoginDate(UserTestConstant.TEST_LAST_LOGIN)
                .auditInfo(auditInfo)
                .build();

        // when
        UserDetailResponseDto userDetailResponseDto = UserDetailResponseDto.fromDomain(user);

        // then
        assertThat(userDetailResponseDto.getUserGuid()).isEqualTo(UserTestConstant.TEST_USER_GUID_1);
        assertThat(userDetailResponseDto.getEmail()).isEqualTo(UserTestConstant.TEST_EMAIL_1);
        assertThat(userDetailResponseDto.getUsername()).isEqualTo(UserTestConstant.TEST_USERNAME_1);
        assertThat(userDetailResponseDto.getIntroduction()).isEqualTo(UserTestConstant.TEST_INTRO_1);
        assertThat(userDetailResponseDto.getPositionList()).containsExactlyElementsOf(UserTestConstant.TEST_POSITION_LIST);
        assertThat(userDetailResponseDto.getSkillList()).containsExactlyElementsOf(UserTestConstant.TEST_SKILL_LIST);
        assertThat(userDetailResponseDto.getMannerDegree()).isEqualTo(UserTestConstant.TEST_MANNER);
        assertThat(userDetailResponseDto.isBlocked()).isEqualTo(UserTestConstant.TEST_BLOCKED);
        assertThat(userDetailResponseDto.getBlockEndDate()).isNull();
        assertThat(userDetailResponseDto.isDeleted()).isEqualTo(UserTestConstant.TEST_DELETED);
        assertThat(userDetailResponseDto.getLastLoginDateTime()).isEqualTo(UserTestConstant.TEST_LAST_LOGIN);
        assertThat(userDetailResponseDto.getRegistrantGuid()).isEqualTo(auditInfo.registrantGuid());
        assertThat(userDetailResponseDto.getRegisteredDate()).isEqualTo(auditInfo.registeredDate());
        assertThat(userDetailResponseDto.getModifierGuid()).isEqualTo(auditInfo.modifierGuid());
        assertThat(userDetailResponseDto.getModifiedDate()).isEqualTo(auditInfo.modifiedDate());
    }
}