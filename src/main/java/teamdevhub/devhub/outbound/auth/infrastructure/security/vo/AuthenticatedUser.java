package teamdevhub.devhub.outbound.auth.infrastructure.security.vo;

import lombok.Builder;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

@Builder
public record AuthenticatedUser(String userGuid, String email, String username, String password, UserRole userRole) {

    public static AuthenticatedUser of(String userGuid, String email, String username, String password, UserRole userRole) {
        return new AuthenticatedUser(userGuid, email, username, password, userRole);
    }
}
