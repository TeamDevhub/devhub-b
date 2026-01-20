package teamdevhub.devhub.adapter.in.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;

import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder
@NoArgsConstructor
public class UserDetailResponseDto extends UserBasicResponseDto {

    private List<String> positionList;
    private List<String> skillList;

    public static UserDetailResponseDto fromDomain(User user) {
        return UserDetailResponseDto.builder()
                .userGuid(user.getUserGuid())
                .email(user.getEmail())
                .username(user.getUsername())
                .introduction(user.getIntroduction())
                .positionList(positionList(user.getPositions()))
                .skillList(skillList(user.getSkills()))
                .mannerDegree(user.getMannerDegree())
                .blocked(user.isBlocked())
                .blockEndDate(user.getBlockEndDate())
                .deleted(user.isDeleted())
                .lastLoginDateTime(user.getLastLoginDate())
                .registrantGuid(user.getAuditInfo().registrantGuid())
                .registeredDate(user.getAuditInfo().registeredDate())
                .modifierGuid(user.getAuditInfo().modifierGuid())
                .modifiedDate(user.getAuditInfo().modifiedDate())
                .build();
    }

    public static List<String> positionList(Set<UserPosition> userPositions) {
        return userPositions.stream()
                .map(UserPosition::positionCd)
                .toList();
    }

    public static List<String> skillList(Set<UserSkill> userSkills) {
        return userSkills.stream()
                .map(UserSkill::skillCd)
                .toList();
    }
}
