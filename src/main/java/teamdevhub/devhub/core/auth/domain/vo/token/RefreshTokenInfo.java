package teamdevhub.devhub.core.auth.domain.vo.token;

import lombok.Builder;

@Builder
public record RefreshTokenInfo(String userGuid) {}
