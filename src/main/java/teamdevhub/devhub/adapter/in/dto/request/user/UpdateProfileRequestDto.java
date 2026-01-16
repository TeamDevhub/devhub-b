package teamdevhub.devhub.adapter.in.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDto {

    private String username;

    private String introduction;

    private List<String> positionList;

    private List<String> skillList;

    public UpdateProfileCommand toUpdateProfileCommand(String userGuid) {
        return UpdateProfileCommand.builder()
                .userGuid(userGuid)
                .username(this.username)
                .introduction(this.introduction)
                .positions(toPositions(this.positionList, userGuid))
                .skills(toSkills(this.skillList, userGuid))
                .build();
    }

    private static Set<UserPosition> toPositions(List<String> positionList, String userGuid) {
        if (positionList == null) {
            return null;
        }
        return positionList.stream()
                .map(positionCd -> new UserPosition(userGuid, positionCd))
                .collect(Collectors.toSet());
    }

    private static Set<UserSkill> toSkills(List<String> skillList, String userGuid) {
        if (skillList == null) {
            return null;
        }
        return skillList.stream()
                .map(skillCd -> new UserSkill(userGuid, skillCd))
                .collect(Collectors.toSet());
    }
}
