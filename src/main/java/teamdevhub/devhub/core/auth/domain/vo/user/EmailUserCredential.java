package teamdevhub.devhub.core.auth.domain.vo.user;

import teamdevhub.devhub.core.user.domain.vo.UserRole;

public record EmailUserCredential(
        String userGuid,
        String email,
        String password,
        UserRole userRole
) {}