package teamdevhub.devhub.port.in.user.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class UpdateProfileCommand {

    private String userGuid;

    private String username;
    private String introduction;

    private Set<UserPosition> positions;
    private Set<UserSkill> skills;

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
