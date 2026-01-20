package teamdevhub.devhub.domain.auth.vo.token;

import teamdevhub.devhub.domain.user.UserRole;

public record AccessTokenInfo(String userGuid, String email, UserRole userRole) {}
