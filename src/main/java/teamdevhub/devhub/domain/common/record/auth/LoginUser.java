package teamdevhub.devhub.domain.common.record.auth;

import teamdevhub.devhub.domain.user.UserRole;

public record LoginUser(String userGuid, String email, String password, UserRole userRole) {
    public static LoginUser of(String userGuid, String email, String password, UserRole userRole) {
        return new LoginUser(userGuid, email, password, userRole);
    }
}
