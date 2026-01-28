package teamdevhub.devhub.core.auth.domain.vo.token;

import lombok.Builder;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

@Builder
public record AccessTokenInfo(String userGuid, String email, UserRole userRole) {}
