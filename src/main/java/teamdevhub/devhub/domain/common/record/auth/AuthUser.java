package teamdevhub.devhub.domain.common.record.auth;

import teamdevhub.devhub.domain.user.UserRole;

public record AuthUser(String userGuid, String email, String password, UserRole userRole) {
    public static AuthUser of(String userGuid, String email, String password, UserRole userRole) {
        return new AuthUser(userGuid, email, password, userRole);
    }
}
