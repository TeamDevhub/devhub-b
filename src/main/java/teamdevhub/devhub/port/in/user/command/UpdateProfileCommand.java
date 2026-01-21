package teamdevhub.devhub.port.in.user.command;

import lombok.Builder;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;

import java.util.Set;

@Builder
public record UpdateProfileCommand(String userGuid,
                                   String username,
                                   String introduction,
                                   Set<UserPosition> positions,
                                   Set<UserSkill> skills
) {
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
