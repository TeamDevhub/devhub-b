package teamdevhub.devhub.domain.user.vo;

import java.util.Set;

public record UserSkillChangeResult(
        boolean changed,
        Set<UserSkill> previousSkills,
        Set<UserSkill> currentSkills
) {
    public static UserSkillChangeResult changed(Set<UserSkill> previous, Set<UserSkill> current) {
        return new UserSkillChangeResult(true, Set.copyOf(previous), Set.copyOf(current));
    }

    public static UserSkillChangeResult unchanged(Set<UserSkill> current) {
        return new UserSkillChangeResult(false, Set.copyOf(current), Set.copyOf(current));
    }
}