package teamdevhub.devhub.domain.user.vo;

import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.exception.DomainRuleException;

public record UserPosition(String userGuid, String positionCd) {

    public UserPosition {
        if (positionCd == null || positionCd.isBlank()) {
            throw DomainRuleException.of(ErrorCode.USER_POSITION_REQUIRED);
        }
    }
}
