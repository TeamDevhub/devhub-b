package teamdevhub.devhub.core.auth.domain.vo.user;

import lombok.Builder;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

@Builder
public record AuthenticatedUser(String userGuid, String loginId, UserRole userRole) {

    public static AuthenticatedUser of(String userGuid, String loginId, UserRole userRole) {
        return new AuthenticatedUser(userGuid, loginId, userRole);
    }
}
