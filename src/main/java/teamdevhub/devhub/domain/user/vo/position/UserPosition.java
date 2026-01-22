package teamdevhub.devhub.domain.user.vo.position;

import lombok.Builder;

@Builder
public record UserPosition(String userGuid, String positionCd) {}
