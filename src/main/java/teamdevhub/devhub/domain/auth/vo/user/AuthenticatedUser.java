package teamdevhub.devhub.domain.auth.vo.user;

import lombok.Builder;
import teamdevhub.devhub.domain.user.UserRole;

@Builder
public record AuthenticatedUser(String userGuid, String email, String password, UserRole userRole) {

    public static AuthenticatedUser of(String userGuid, String email, String password, UserRole userRole) {
        return new AuthenticatedUser(userGuid, email, password, userRole);
    }
}
