package teamdevhub.devhub.domain.common.vo.auth;

import teamdevhub.devhub.domain.user.UserRole;

public record AuthenticatedUser(String userGuid, String email, String password, UserRole userRole) {
    public static AuthenticatedUser of(String userGuid, String email, String password, UserRole userRole) {
        return new AuthenticatedUser(userGuid, email, password, userRole);
    }
}
