package teamdevhub.devhub.domain.auth.vo.token;

import lombok.Builder;

@Builder
public record RefreshTokenInfo(String userGuid) {}
