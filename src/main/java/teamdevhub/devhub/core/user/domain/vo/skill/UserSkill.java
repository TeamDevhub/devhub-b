package teamdevhub.devhub.core.user.domain.vo.skill;

import lombok.Builder;

@Builder
public record UserSkill(String userGuid, String skillCd) {}
