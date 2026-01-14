package teamdevhub.devhub.domain.user.vo;

import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.exception.DomainRuleException;

public record UserSkill(String userGuid, String skillCd) {}
