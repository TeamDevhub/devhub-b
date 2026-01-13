package teamdevhub.devhub.port.in.user.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class UpdateProfileCommand {

    private String userGuid;

    private String username;
    private String introduction;

    private Set<UserPosition> positions;
    private Set<UserSkill> skills;

    public static UpdateProfileCommand fromUpdateProfileRequestDto(
            UpdateProfileRequestDto updateProfileRequestDto,
            String userGuid
    ) {
        return UpdateProfileCommand.builder()
                .userGuid(userGuid)
                .username(updateProfileRequestDto.getUsername())
                .introduction(updateProfileRequestDto.getIntroduction())
                .positions(toPositions(updateProfileRequestDto.getPositionList(), userGuid))
                .skills(toSkills(updateProfileRequestDto.getSkillList(), userGuid))
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

    public boolean hasUsernameAndIntroductionChange() {
        return username != null || introduction != null;
    }

    public boolean hasPositionsChange() {
        return positions != null;
    }

    public boolean hasSkillsChange() {
        return skills != null;
    }
}
