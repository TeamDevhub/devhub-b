package teamdevhub.devhub.domain.user.vo.skill;

import lombok.Builder;

@Builder
public record UserSkill(String userGuid, String skillCd) {}
