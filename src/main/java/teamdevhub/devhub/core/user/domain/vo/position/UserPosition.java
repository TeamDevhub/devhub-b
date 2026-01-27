package teamdevhub.devhub.core.user.domain.vo.position;

import lombok.Builder;

@Builder
public record UserPosition(String userGuid, String positionCd) {}
