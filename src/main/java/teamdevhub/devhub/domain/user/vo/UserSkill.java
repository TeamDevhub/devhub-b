package teamdevhub.devhub.domain.user.vo;

import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.exception.DomainRuleException;

public record UserSkill(String userGuid, String skillCd) {

    public UserSkill {
        if (skillCd == null || skillCd.isBlank()) {
            throw DomainRuleException.of(ErrorCode.USER_SKILL_REQUIRED);
        }
    }
}
